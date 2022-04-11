package nl.ealse.ccnl.event;

import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class MenuChoiceEvent extends ApplicationEvent {

  @Getter
  private final MenuChoice menuChoice; // NOSONAR

  public MenuChoiceEvent(Object source, MenuChoice menuChoice) {
    super(source);
    this.menuChoice = menuChoice;
  }

  /**
   * Called by a {@link org.springframework.context.event.EventListener#condition()}
   */
  public boolean name(String... eventNames) {
    for (String eventName : eventNames) {
      if (menuChoice.name().equals(eventName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Called by a {@link org.springframework.context.event.EventListener#condition()}
   */
  public boolean group(String groupName) {
    return menuChoice.getGroup() != null && menuChoice.getGroup().name().equals(groupName);
  }

}
