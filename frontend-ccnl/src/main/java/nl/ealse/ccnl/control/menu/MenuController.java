package nl.ealse.ccnl.control.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;

/**
 * Controller for all menu bar choices.
 */
public class MenuController {
  public static final String SOURCE = "MENU_CONTROLLER";

  @FXML
  void action(ActionEvent evt) {
    MenuItem source = (MenuItem) evt.getSource();
    EventPublisher.publishEvent(new MenuChoiceEvent(SOURCE, MenuChoice.valueOf(source.getId())));
  }

}
