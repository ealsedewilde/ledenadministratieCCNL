package nl.ealse.ccnl.event;

import java.util.EventObject;
import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;

/**
 * Super type of all events that are used in the application flow.
 */
@SuppressWarnings("serial")
public class MenuChoiceEvent extends EventObject {

  @Getter
  private final MenuChoice menuChoice; // NOSONAR

  public MenuChoiceEvent(Object source, MenuChoice menuChoice) {
    super(source);
    this.menuChoice = menuChoice;
  }

  public boolean hasGroup() {
    return menuChoice.getGroup() != null;
  }

}
