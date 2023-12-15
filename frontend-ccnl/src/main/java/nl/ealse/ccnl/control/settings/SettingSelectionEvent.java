package nl.ealse.ccnl.control.settings;

import java.util.EventObject;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

@SuppressWarnings("serial")
public class SettingSelectionEvent extends EventObject {
  
  @Getter
  private final transient Setting selectedSetting;

  public SettingSelectionEvent(Object source, Setting setting) {
    super(source);
    this.selectedSetting = setting;
  }

}
