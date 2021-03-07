package nl.ealse.javafx.mappers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextInputControlStringMapperTest {

  private final TextInputControlStringMapper sut = new TextInputControlStringMapper();

  private final String s = "Test data";

  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    TextField tf = new TextField();
    tf.setText(s);
    String result = sut.getPropertyFromJavaFx(tf);
    Assertions.assertEquals(s, result);
  }

  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    TextField tf = new TextField();
    sut.mapPropertyToJavaFx(s, tf);
    Assertions.assertEquals(s, tf.getText());
  }

}
