package nl.ealse.ccnl.control.settings;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ioc.ComponentProviderUtil;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class SettingsControllerTest extends FXMLBaseTest {

  private static SettingsService service;
  private static MouseEvent mouseEvent;

  private static Setting setting;

  private SettingsController sut;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(this, MenuChoice.SETTINGS);
    sut.findSettings(event);

    try (MockedStatic<EventPublisher> context = mockStatic(EventPublisher.class)) {
      sut.editSetting(mouseEvent);
      context.verify(() -> EventPublisher.publishEvent(any(SettingSelectionEvent.class)));
    }

    sut.save();
    verify(getPageController(), never()).showMessage("Instelling is opgeslagen");
    setInput();
    sut.save();
    verify(getPageController()).showMessage("Instelling is opgeslagen");

    reset(getPageController());
    sut.delete(setting());
    verify(getPageController()).showMessage("Instelling is verwijderd");

    sut.update(setting(), "foo");
    verify(getPageController()).showMessage("Instelling is bijgewerkt");
  }

  private void prepare() {
    sut = getTestSubject(SettingsController.class);
    getPageWithFxController(sut, PageName.SETTINGS);
    TableRow<Setting> row = new TableRow<>();
    row.setItem(setting);
    when(mouseEvent.getSource()).thenReturn(row);
  }

  @BeforeAll
  static void setup() {
    service = ComponentProviderUtil.getComponent(SettingsService.class);
    setting = setting();
    List<Setting> settings = new ArrayList<>();
    settings.add(setting);
    when(service.findByOrderBySettingsGroupAscKeyAsc()).thenReturn(settings);
    mouseEvent = mock(MouseEvent.class);
  }

  private static Setting setting() {
    Setting s = new Setting();
    s.setDescription("Setting1");
    s.setKey("key1");
    s.setSettingsGroup("group1");
    s.setValue("value1");
    s.prePersist();
    return s;
  }

  private void setInput() {
    sut.getKey().setText("key2");
    sut.getGroup().setText("group2");
    sut.getDescription().setText("Some description");
    sut.getValue().setText("value2");
  }


}
