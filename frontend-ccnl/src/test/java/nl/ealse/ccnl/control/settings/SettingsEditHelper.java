package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import javafx.scene.Parent;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;

@Getter
public class SettingsEditHelper extends FXMLBaseTest<SettingsEdit> {
  
  private static SettingsEditHelper INSTANCE = new SettingsEditHelper();
  
  private SettingsController parentController;
  
  private SettingsEdit settingsEdit;
    
  private Parent page;
  
  private SettingsEditHelper() {
    
  }
  
  public synchronized Parent loadSettingsEditPage() {
    if (page == null) {
      parentController = mock(SettingsController.class);
      settingsEdit = new SettingsEdit(parentController);
      try {
        page = getPage(settingsEdit, PageName.SETTINGS_EDIT);
      } catch (FXMLMissingException e) {
        Assertions.fail(e.getMessage());
      }
    }
    return page;
  }
  
  public static SettingsEditHelper getInstance() {
    return INSTANCE;
  }

}
