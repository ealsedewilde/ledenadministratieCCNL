package nl.ealse.javafx.mappers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ComboBoxIntegerMapperTest {
  
  private ComboBoxIntegerMapper sut = new ComboBoxIntegerMapper();
  
  @Test
  void getPropertyFromJavaFx() {
    new JFXPanel();
    ComboBox<Integer> box = new ComboBox<>();
    box.setValue(1234);
    int result = sut.getPropertyFromJavaFx(box);
    Assertions.assertEquals(1234, result);
  }
  
  @Test
  void mapPropertyToJavaFx() {
    new JFXPanel();
    ComboBox<Integer> box = new ComboBox<>();
    sut.mapPropertyToJavaFx(1234, box);
    int result = box.getValue();    
    Assertions.assertEquals(1234, result);
  }



}
