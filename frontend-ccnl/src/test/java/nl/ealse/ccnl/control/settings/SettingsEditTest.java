package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class SettingsEditTest extends FXMLBaseTest<SettingsEdit> {
  
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
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      SettingsEditHelper seh = SettingsEditHelper.getInstance();
      seh.loadSettingsEditPage();
      sut = seh.getSettingsEdit();
      parentController = seh.getParentController();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    SettingSelectionEvent event = new SettingSelectionEvent(sut, setting);
    sut.onApplicationEvent(event);

    sut.update();
    verify(parentController).update(setting, id);

    sut.delete();
  }

}
