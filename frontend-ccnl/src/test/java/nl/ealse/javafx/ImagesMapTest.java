package nl.ealse.javafx;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImagesMapTest {

  @Test
  void getImage() {
    Image img = ImagesMap.get("Citroen.png");
    Assertions.assertNotNull(img);

    Assertions.assertThrows(FXMLLoadException.class, () -> ImagesMap.get("Missing.png"));

  }

}
