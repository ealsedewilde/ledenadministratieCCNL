package nl.ealse.ccnl.control.magazine;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.service.excelexport.ExportAddressService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AddressListController {

  private static final String FILE_NAME = "adressen_clubblad_%s.xlsx";
  private static final String CARD_FILE_NAME = "adressen_lidmaatschap_%d.xlsx";

  private static final String SETTING_GROUP = "magazine";
  private static final String SETTING_KEY = "number";

  @Value("${ccnl.directory.magazine:c:/temp}")
  private String magazineDirectory;

  private final PageController pageController;

  private final ExportAddressService magazineService;

  private final SettingsService service;

  private final TaskExecutor executor;

  private WrappedFileChooser fileChooser;

  @FXML
  private TextField magazineNumber;
  @FXML
  private Label magazineNumberE;

  public AddressListController(SettingsService service, PageController pageController,
      ExportAddressService magazineService, TaskExecutor executor) {
    this.pageController = pageController;
    this.magazineService = magazineService;
    this.service = service;
    this.executor = executor;
  }

  @EventListener(condition = "#event.name('MAGAZINE_ADDRESS_LIST')")
  public void addressList(MenuChoiceEvent event) {
    Optional<Setting> previousNumber = service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY);
    if (previousNumber.isPresent()) {
      int n = Integer.parseInt(previousNumber.get().getValue());
      n++;
      magazineNumber.setText(Integer.toString(n));
    } else {
      magazineNumber.setText("9999");
    }

  }

  @EventListener(condition = "#event.name('CARD_ADDRESS_LIST')")
  public void cardList(MenuChoiceEvent event) {
    fileChooser.setInitialFileName(String.format(CARD_FILE_NAME, LocalDate.now().getYear()));
    File addressFile = fileChooser.showSaveDialog();
    if (addressFile != null) {
      pageController.showPermanentMessage("Bestand wordt aangemaakt; even geduld a.u.b.");
      AsyncCardAddressListTask asyncTask =
          new AsyncCardAddressListTask(magazineService, addressFile);
      asyncTask
          .setOnSucceeded(evt -> pageController.showMessage(evt.getSource().getValue().toString()));
      asyncTask.setOnFailed(
          evt -> pageController.showErrorMessage(evt.getSource().getException().getMessage()));
      executor.execute(asyncTask);
      pageController.setActivePage(PageName.LOGO);
    }
  }

  @FXML
  public void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XLSX);
    fileChooser.setInitialDirectory(new File(magazineDirectory));
  }

  /**
   * Generate the address file with the selected magazineNumber.
   */
  public void generateAddressList() {
    if (valid()) {
      fileChooser.setInitialFileName(String.format(FILE_NAME, magazineNumber.getText()));
      File addressFile = fileChooser.showSaveDialog();
      if (addressFile != null) {
        pageController.showPermanentMessage("Bestand wordt aangemaakt; even geduld a.u.b.");
        AsyncAddressListTask asyncTask = new AsyncAddressListTask(this, addressFile);
        asyncTask.setOnSucceeded(
            evt -> pageController.showMessage(evt.getSource().getValue().toString()));
        asyncTask.setOnFailed(
            evt -> pageController.showErrorMessage(evt.getSource().getException().getMessage()));
        executor.execute(asyncTask);
        pageController.setActivePage(PageName.LOGO);
      }
    }
  }

  private boolean valid() {
    try {
      Integer.valueOf(magazineNumber.getText());
      magazineNumberE.setVisible(false);
      return true;
    } catch (NumberFormatException e) {
      magazineNumberE.setVisible(true);
    }
    return false;
  }

  protected static class AsyncAddressListTask extends Task<String> {
    private final AddressListController controller;
    private final File addressFile;

    AsyncAddressListTask(AddressListController controller, File addressFile) {
      this.controller = controller;
      this.addressFile = addressFile;
    }

    @Override
    protected String call() throws Exception {
      try {
        controller.magazineService.generateMagazineAddressFile(addressFile);
        Setting setting = getSetting();
        setting.setValue(controller.magazineNumber.getText());
        controller.service.save(setting);
        return "Bestand is aangemaakt";
      } catch (IOException e) {
        log.error("error creating Excel file", e);
        throw new AsyncTaskException("Bestand aanmaken is mislukt");
      }
    }


    private Setting getSetting() {
      Optional<Setting> previousNumber =
          controller.service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY);
      if (previousNumber.isPresent()) {
        return previousNumber.get();
      }
      Setting setting = new Setting();
      setting.setDescription("Volgnummer clubblad");
      setting.setKey(SETTING_KEY);
      setting.setSettingsGroup(SETTING_GROUP);
      return setting;
    }
  }

  protected static class AsyncCardAddressListTask extends Task<String> {
    private final ExportAddressService magazineService;
    private final File addressFile;

    AsyncCardAddressListTask(ExportAddressService magazineService, File addressFile) {
      this.magazineService = magazineService;
      this.addressFile = addressFile;
    }

    @Override
    protected String call() throws Exception {
      try {
        magazineService.generateCardAddressFile(addressFile);
        return "Bestand is aangemaakt";
      } catch (IOException e) {
        log.error("error creating Excel file", e);
        throw new AsyncTaskException("Bestand aanmaken is mislukt");
      }
    }

  }

}
