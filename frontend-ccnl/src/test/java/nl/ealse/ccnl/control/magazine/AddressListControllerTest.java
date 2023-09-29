package nl.ealse.ccnl.control.magazine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.magazine.AddressListController.AsyncAddressListTask;
import nl.ealse.ccnl.control.magazine.AddressListController.AsyncCardAddressListTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.service.excelexport.ExportAddressService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;

class AddressListControllerTest extends FXMLBaseTest<AddressListController> {

  private static final String SETTING_GROUP = "magazine";
  private static final String SETTING_KEY = "number";

  private static PageController pageController;
  private static ExportAddressService magazineService;
  private static SettingsService service;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor step1Executor = new TestExecutor<AsyncAddressListTask>();
  private static TaskExecutor step2Executor = new TestExecutor<AsyncCardAddressListTask>();
  private static TaskExecutor executor = task -> {
    if ( task instanceof AsyncAddressListTask) {
      step1Executor.execute(task);
    } else {
      step2Executor.execute(task);
    }
  };

  private AddressListController sut;

  @Test
  void testController() {
    sut = new AddressListController(service, pageController, magazineService, executor);
    magazineDirectory();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MAGAZINE_ADDRESS_LIST);
    sut.addressList(event);
    sut.generateAddressList();
    verify(pageController).showMessage("Bestand is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.CARD_ADDRESS_LIST);
    sut.cardList(event);
    verify(pageController, times(2)).showMessage("Bestand is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.MEMBER_LIST_BY_NAME);
    sut.memberListByName(event);
    verify(pageController, times(3)).showMessage("Bestand is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.MEMBER_LIST_BY_NUMBER);
    sut.memberListByNumber(event);
    verify(pageController, times(4)).showMessage("Bestand is aangemaakt");
  }

  private void prepare() {
    try {
      getPageWithFxController(sut, PageName.MAGAZINE_ADDRESS_LIST);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    magazineService = mock(ExportAddressService.class);
    service = mock(SettingsService.class);
    Setting s = new Setting();
    s.setKey(SETTING_KEY);
    s.setSettingsGroup(SETTING_GROUP);
    s.setValue("400");
    when(service.getSetting(Optional.of(SETTING_GROUP), SETTING_KEY)).thenReturn(Optional.of(s));
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(new File("address.xlsx"));

  }

  private void magazineDirectory() {
    try {
      FieldUtils.writeField(sut, "magazineDirectory", "C:/temp", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
