package nl.ealse.ccnl.control.button;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import nl.ealse.javafx.ImagesMap;

public class ImageButton extends Button {

  public ImageButton(String iconName, String buttonText) {
    ImageView image = new ImageView(ImagesMap.get(iconName));
    image.setFitHeight(30d);
    image.setPreserveRatio(true);
    this.setGraphic(image);
    this.setText(buttonText);
  }

}
