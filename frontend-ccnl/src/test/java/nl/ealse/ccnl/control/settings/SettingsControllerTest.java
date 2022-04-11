package nl.ealse.ccnl.control.settings;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import nl.ealse.ccnl.service.SettingsService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class SettingsControllerTest extends FXMLBaseTest<SettingsController> {

  private static PageController pageController;
  private static ApplicationContext context;
  private static SettingsService service;
  private static MouseEvent mouseEvent;
  
  private static Setting setting;

  private SettingsController sut;
 
  @Test
  void testController() {
    sut = new SettingsController(service, pageController, context);
    setting();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(this, MenuChoice.SETTINGS);
    sut.findSettings(event);

    sut.editSetting(mouseEvent);
    verify(context).publishEvent(any(SettingSelectionEvent.class));

    sut.save();
    verify(pageController, never()).showMessage("Instelling is opgeslagen; actief na herstart");
    setInput();
    sut.save();
    verify(pageController).showMessage("Instelling is opgeslagen; actief na herstart");

    sut.delete(setting());
    verify(pageController).showMessage("Instelling is verwijderd; actief na herstart");

    sut.update(setting(), "foo");
    verify(pageController).showMessage("Instelling is bijgewerkt; actief na herstart");
  }

  private void prepare() {
    try {
      SettingsEditHelper seh = SettingsEditHelper.getInstance();
      Parent p = seh.loadSettingsEditPage();
      when(pageController.loadPage(PageName.SETTINGS_EDIT)).thenReturn(p);
      getPage(sut, PageName.SETTINGS);
      TableRow<Setting> row = new TableRow<>();
      row.setItem(setting);
      when(mouseEvent.getSource()).thenReturn(row);
    } catch (FXMLMissingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    context = mock(ApplicationContext.class);
    service = mock(SettingsService.class);
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
