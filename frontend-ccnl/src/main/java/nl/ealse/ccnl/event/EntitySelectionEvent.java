package nl.ealse.ccnl.event;

import lombok.Getter;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.MenuController;

/**
 * Entity selected for processing in the various screens. For new entities it is the
 * {@link MenuController} that provides an initial entity. For other cases the entity is the result
 * of a search process.
 *
 * @author ealse
 *
 * @param <T> selected model object
 */
@SuppressWarnings("serial")
public class EntitySelectionEvent<T> extends MenuChoiceEvent {

  @Getter
  private final transient T selectedEntity; // NOSONAR

  public EntitySelectionEvent(Object source, MenuChoice menuChoice, T selectedEntity) {
    super(source, menuChoice);
    this.selectedEntity = selectedEntity;
  }
  
  /**
   * This type of event is never a group event.
   */
  @Override
  public boolean group(String groupName) {
    return false;
  }


}
