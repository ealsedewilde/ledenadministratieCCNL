package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingsEditTest extends FXMLBaseTest {

  private static final String id = "id";

  private SettingsController parentController;

  private SettingsEdit sut;

  private Setting setting;

  @Test
  void testController() {
    setting = new Setting();
    setting.setId(id);
    setting.setKey(id);
    setting.setValue("value");

    Assertions.assertTrue(runFX(() -> {
      prepare();
      doTest();
      return Boolean.TRUE;
    }));
    
  }

  private void doTest() {
    SettingSelectionEvent event = new SettingSelectionEvent(sut, setting);
    sut.onApplicationEvent(event);

    sut.update();
    verify(getParentService()).save(setting, id);

    sut.delete();
  }

  public void prepare() {
    parentController = getTestSubject(SettingsController.class);
    getPageWithFxController(parentController, PageName.SETTINGS);
    sut = new SettingsEdit(parentController);
  }

  private SettingsService getParentService() {
    try {
      return (SettingsService) FieldUtils.readDeclaredField(parentController, "service", true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

}
