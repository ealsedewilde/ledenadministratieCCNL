package nl.ealse.javafx.mappers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LabelStringMapperTest {

  private final LabelStringMapper sut = new LabelStringMapper();

  private final String s = "Test data";

  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    Label lbl = new Label();
    lbl.setText(s);
    String result = sut.getPropertyFromJavaFx(lbl);
    Assertions.assertEquals(s, result);
  }

  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    Label lbl = new Label();
    sut.mapPropertyToJavaFx(s, lbl);
    Assertions.assertEquals(s, lbl.getText());
  }

}
