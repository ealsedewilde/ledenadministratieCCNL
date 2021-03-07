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

}
