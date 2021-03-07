package nl.ealse.javafx.mappers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LabelIntegerMapperTest {

  private final LabelIntegerMapper sut = new LabelIntegerMapper();

  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    Label lbl = new Label();
    lbl.setText("1234");
    Integer result = sut.getPropertyFromJavaFx(lbl);
    Assertions.assertEquals(Integer.valueOf(1234), result);
  }

  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    Label lbl = new Label();
    sut.mapPropertyToJavaFx(Integer.valueOf(1234), lbl);
    Assertions.assertEquals("1234", lbl.getText());
  }

}
