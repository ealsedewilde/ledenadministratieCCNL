package nl.ealse.ccnl.control.settings;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Setting;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class SettingSelectionEvent extends ApplicationEvent {
  
  @Getter
  private final Setting selectedSetting;

  public SettingSelectionEvent(Object source, Setting setting) {
    super(source);
    this.selectedSetting = setting;
  }

}
