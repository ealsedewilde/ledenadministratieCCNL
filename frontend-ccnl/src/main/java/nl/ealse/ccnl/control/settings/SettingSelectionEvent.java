package nl.ealse.ccnl.control.settings;

import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.EntitySelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

@SuppressWarnings("serial")
public class SettingSelectionEvent extends EntitySelectionEvent<Setting> {

  public SettingSelectionEvent(Object source, Setting setting) {
    super(source, MenuChoice.SETTINGS, setting);
  }

}
