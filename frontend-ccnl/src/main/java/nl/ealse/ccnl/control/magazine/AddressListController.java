package nl.ealse.ccnl.control.magazine;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.service.excelexport.ExportAddressService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AddressListController implements ApplicationListener<MenuChoiceEvent> {

  private static final String FILE_NAME = "adressen_clubblad_%s.xlsx";
  private static final String CARD_FILE_NAME = "adressen_lidmaatschap_%d.xlsx";

  private static final String SETTING_GROUP = "magazine";
  private static final String SETTING_KEY = "number";

  @Value("${ccnl.directory.magazine:c:/temp}")
  private String magazineDirectory;

  private final PageController pageController;

  private final ExportAddressService magazineService;

  private final SettingsService service;

  private WrappedFileChooser fileChooser;

  @FXML
  private TextField magazineNumber;
  @FXML
  private Label magazineNumberE;

  public AddressListController(SettingsService service, PageController pageController,
      ExportAddressService magazineService) {
    this.pageController = pageController;
    this.magazineService = magazineService;
    this.service = service;
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.MAGAZINE_ADDRESS_LIST == event.getMenuChoice()) {
      Optional<Setting> previousNumber =
          service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY);
      if (previousNumber.isPresent()) {
        int n = Integer.parseInt(previousNumber.get().getValue());
        n++;
        magazineNumber.setText(Integer.toString(n));
      } else {
        magazineNumber.setText("9999");
      }
    } else if (MenuChoice.CARD_ADDRESS_LIST == event.getMenuChoice()) {
      generateMemberCardList();
    }

  }

  @FXML
  public void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XLSX);
    fileChooser.setInitialDirectory(new File(magazineDirectory));
  }

  public void generateAddressList() {
    if (valid()) {
      fileChooser.setInitialFileName(String.format(FILE_NAME, magazineNumber.getText()));
      File addressFile = fileChooser.showSaveDialog();
      if (addressFile != null) {
        try {
          magazineService.generateMagazineAddressFile(addressFile);
          Setting setting = getSetting();
          setting.setValue(magazineNumber.getText());
          service.save(setting);
          pageController.showMessage("Bestand is aangemaakt");
          pageController.setActivePage(PageName.LOGO);
        } catch (IOException e) {
          log.error("error creating Excel file", e);
          pageController.showErrorMessage("Bestand aanmaken is mislukt");
        }
      }
    }
  }

  public void generateMemberCardList() {
    fileChooser.setInitialFileName(String.format(CARD_FILE_NAME, LocalDate.now().getYear()));
    File addressFile = fileChooser.showSaveDialog();
    if (addressFile != null) {
      try {
        magazineService.generateCardAddressFile(addressFile);
        Setting setting = getSetting();
        setting.setValue(magazineNumber.getText());
        service.save(setting);
        pageController.showMessage("Bestand is aangemaakt");
        pageController.setActivePage(PageName.LOGO);
      } catch (IOException e) {
        log.error("error creating Excel file", e);
        pageController.showErrorMessage("Bestand aanmaken is mislukt");
      }
    }
  }

  private Setting getSetting() {
    Optional<Setting> previousNumber = service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY);
    if (previousNumber.isPresent()) {
      return previousNumber.get();
    }
    Setting setting = new Setting();
    setting.setDescription("Volgnummer clubblad");
    setting.setKey(SETTING_KEY);
    setting.setSettingsGroup(SETTING_GROUP);
    return setting;
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

}
