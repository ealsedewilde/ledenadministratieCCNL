package nl.ealse.ccnl.control.button;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nl.ealse.javafx.ImagesMap;

public class ImageCell<T> extends TableCell<T, Void> {

  private final ImageView view;

  public ImageCell(String imageName, EventHandler<MouseEvent> eventHandler) {
    this.view = new ImageView(ImagesMap.get(imageName));
    this.setOnMouseClicked(eventHandler);
  }

  @Override
  protected void updateItem(Void item, boolean empty) {
    super.updateItem(item, empty);
    if (!empty) {
      setGraphic(view);
    } else {
      setGraphic(null);
    }
  }
}
