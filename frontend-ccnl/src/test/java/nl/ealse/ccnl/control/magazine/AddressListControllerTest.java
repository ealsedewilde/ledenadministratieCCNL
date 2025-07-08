package nl.ealse.ccnl.control.magazine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.Optional;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.service.excelexport.ExportAddressService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AddressListControllerTest extends FXMLBaseTest {

  private static final String SETTING_GROUP = "magazine";
  private static final String SETTING_KEY = "number";

  private static SettingsService service;
  private static WrappedFileChooser fileChooser;


  private AddressListController sut;

  @Test
  void testController() {
    Assertions.assertTrue(runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
    }));

  }

  private void doTest() {
    sut.addressList();
    sut.generateAddressList();
    verify(getPageController()).showMessage("Bestand is aangemaakt");

    sut.cardList();
    verify(getPageController(), times(2)).showMessage("Bestand is aangemaakt");

    sut.memberListByName();
    verify(getPageController(), times(3)).showMessage("Bestand is aangemaakt");

    sut.memberListByNumber();
    verify(getPageController(), times(4)).showMessage("Bestand is aangemaakt");
  }

  private void prepare() {
    reset(getPageController());
    sut = getTestSubject(AddressListController.class);
    getPageWithFxController(sut, PageName.MAGAZINE_ADDRESS_LIST);
  }

  @BeforeAll
  static void setup() {

    mock(ExportAddressService.class);
    service = ApplicationContext.getComponent(SettingsService.class);
    Setting s = new Setting();
    s.setKey(SETTING_KEY);
    s.setSettingsGroup(SETTING_GROUP);
    s.setValue("400");
    when(service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY)).thenReturn(Optional.of(s));
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(new File("address.xlsx"));
  }


  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
