package nl.ealse.ccnl.control.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonCell<T, S> extends TableCell<T, S> {

  private final Button button;

  public ButtonCell(Button button, EventHandler<ActionEvent> eventHandler) {
    this.button = button;
    this.button.setOnAction(eventHandler);
  }

  @Override
  protected void updateItem(S item, boolean empty) {
    super.updateItem(item, empty);
    if (!empty) {
      setGraphic(button);
    } else {
      setGraphic(null);
    }
  }

}
