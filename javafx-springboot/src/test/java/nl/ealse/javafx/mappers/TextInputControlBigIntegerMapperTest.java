package nl.ealse.javafx.mappers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextInputControlBigIntegerMapperTest {

  private TextInputControlIntegerMapper sut = new TextInputControlIntegerMapper();

  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    TextField tf = new TextField();
    tf.setText("1234");
    Integer result = sut.getPropertyFromJavaFx(tf);
    Assertions.assertEquals(Integer.valueOf(1234), result);
  }

  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    Integer source = Integer.valueOf(1234);
    TextField tf = new TextField();
    sut.mapPropertyToJavaFx(source, tf);
    Assertions.assertEquals("1234", tf.getText());
  }

}
