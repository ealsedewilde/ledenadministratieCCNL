package nl.ealse.ccnl.control.magazine;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.service.excelexport.ExportAddressService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class AddressListController {

  private static final String MAGAZINE_FILE_NAME = "adressen_clubblad_%s.xlsx";
  private static final String CARD_FILE_NAME = "adressen_lidmaatschap_%d.xlsx";
  private static final String MEMBER_FILE_NUMBER = "ledenlijst_op_nummer_%d.xlsx";
  private static final String MEMBER_FILE_NAME = "ledenlijst_op_naam_%d.xlsx";

  private static final String SETTING_GROUP = "ccnl.magazine";
  private static final String SETTING_KEY = "number";

  private final PageController pageController;

  private final ExportAddressService magazineService;

  private final SettingsService service;

  private WrappedFileChooser fileChooser;

  @FXML
  private TextField magazineNumber;
  @FXML
  private Label magazineNumberE;

  @FXML
  private CheckBox passColumn;

  public AddressListController(PageController pageController, ExportAddressService magazineService,
      SettingsService service) {
    this.pageController = pageController;
    this.magazineService = magazineService;
    this.service = service;
    setup();
  }

  void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.XLSX);
    fileChooser.setInitialDirectory(
        () -> ApplicationContext.getPreference("ccnl.directory.magazine", "c:/temp"));
  }

  @EventListener(menuChoice = MenuChoice.MAGAZINE_ADDRESS_LIST)
  public void addressList(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.MAGAZINE_ADDRESS_LIST);
    Optional<Setting> previousNumber = service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY);
    if (previousNumber.isPresent()) {
      int n = Integer.parseInt(previousNumber.get().getValue());
      n++;
      magazineNumber.setText(Integer.toString(n));
    } else {
      magazineNumber.setText("9999");
    }

  }

  @EventListener(menuChoice = MenuChoice.CARD_ADDRESS_LIST)
  public void cardList(MenuChoiceEvent event) {
    AsyncCardAddressListTask asyncTask = new AsyncCardAddressListTask(magazineService);
    generateFile(String.format(CARD_FILE_NAME, LocalDate.now().getYear()), asyncTask);
  }

  @EventListener(menuChoice = MenuChoice.MEMBER_LIST_BY_NUMBER)
  public void memberListByNumber(MenuChoiceEvent event) {
    MemberListTask asyncTask = new MemberListTask(magazineService, false);
    generateFile(String.format(MEMBER_FILE_NUMBER, LocalDate.now().getYear()), asyncTask);
  }

  @EventListener(menuChoice = MenuChoice.MEMBER_LIST_BY_NAME)
  public void memberListByName(MenuChoiceEvent event) {
    MemberListTask asyncTask = new MemberListTask(magazineService, true);
    generateFile(String.format(MEMBER_FILE_NAME, LocalDate.now().getYear()), asyncTask);
  }

  private void generateFile(String fileName, FileTask task) {
    fileChooser.setInitialFileName(fileName);
    File addressFile = fileChooser.showSaveDialog();
    if (addressFile != null) {
      task.setAddressFile(addressFile);
      pageController.showPermanentMessage("Bestand wordt aangemaakt; even geduld a.u.b.");
      task.executeTask();
      pageController.activateLogoPage();
    }
  }

  /**
   * Generate the address file with the selected magazineNumber.
   */
  public void generateAddressList() {
    if (valid()) {
      AsyncAddressListTask asyncTask = new AsyncAddressListTask(this);
      generateFile(String.format(MAGAZINE_FILE_NAME, magazineNumber.getText()), asyncTask);
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

  protected abstract static class FileTask extends HandledTask {
    @Setter
    protected File addressFile;

    @Override
    protected String call() {
      try {
        executeWork();
        return "Bestand is aangemaakt";
      } catch (IOException e) {
        log.error("error creating Excel file", e);
        throw new AsyncTaskException("Bestand aanmaken is mislukt");
      }
    }

    protected abstract void executeWork() throws IOException;

  }

  protected static class AsyncAddressListTask extends FileTask {
    private final AddressListController controller;

    AsyncAddressListTask(AddressListController controller) {
      this.controller = controller;
    }

    @Override
    protected void executeWork() throws IOException {
      controller.magazineService.generateMagazineAddressFile(addressFile,
          controller.passColumn.isSelected());
      Setting setting = getSetting();
      setting.setValue(controller.magazineNumber.getText());
      controller.service.save(setting);
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

  protected static class AsyncCardAddressListTask extends FileTask {
    private final ExportAddressService magazineService;

    AsyncCardAddressListTask(ExportAddressService magazineService) {
      this.magazineService = magazineService;
    }

    @Override
    protected void executeWork() throws IOException {
      magazineService.generateCardAddressFile(addressFile);
    }

  }

  protected static class MemberListTask extends FileTask {
    private final ExportAddressService magazineService;
    private final boolean byName;

    MemberListTask(ExportAddressService magazineService, boolean byName) {
      this.magazineService = magazineService;
      this.byName = byName;
    }

    @Override
    protected void executeWork() throws IOException {
      if (byName) {
        magazineService.generateMemberListFileByName(addressFile);
      } else {
        magazineService.generateMemberListFileByNumber(addressFile);
      }
    }

  }


}
