package nl.ealse.javafx.mappers;

import java.time.LocalDate;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextInputControlLocalDateMapperTest {

  private TextInputControlLocalDateMapper sut = new TextInputControlLocalDateMapper();

  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    TextField tf = new TextField();
    tf.setText("05-12-2020");
    LocalDate result = sut.getPropertyFromJavaFx(tf);
    Assertions.assertEquals(LocalDate.of(2020, 12, 5), result);
  }

  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    LocalDate source = LocalDate.of(2020, 12, 5);
    TextField tf = new TextField();
    sut.mapPropertyToJavaFx(source, tf);
    Assertions.assertEquals("05-12-2020", tf.getText());
  }

}
