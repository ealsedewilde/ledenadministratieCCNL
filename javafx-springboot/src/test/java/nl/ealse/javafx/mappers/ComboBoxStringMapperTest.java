package nl.ealse.javafx.mappers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ComboBoxStringMapperTest {
  
  private ComboBoxStringMapper sut = new ComboBoxStringMapper();
  
  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    ComboBox<String> box = new ComboBox<>();
    box.setValue("1234");
    String result = sut.getPropertyFromJavaFx(box);
    Assertions.assertEquals("1234", result);
  }
  
  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    ComboBox<String> box = new ComboBox<>();
    sut.mapPropertyToJavaFx("1234", box);
    String result = box.getValue();    
    Assertions.assertEquals("1234", result);
  }



}
