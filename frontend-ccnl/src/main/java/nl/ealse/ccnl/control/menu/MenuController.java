package nl.ealse.ccnl.control.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import lombok.Getter;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;

/**
 * Controller for all menu bar choices.
 */
public class MenuController {
  
  @Getter
  private static final MenuController instance = new MenuController();
  
  private MenuController() {}
  
  @FXML
  void action(ActionEvent evt) {
    MenuItem source = (MenuItem) evt.getSource();
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.valueOf(source.getId())));
  }

}
