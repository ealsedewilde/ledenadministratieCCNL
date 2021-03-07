package nl.ealse.javafx.mappers;

import javafx.scene.control.ComboBox;

public class ComboBoxStringMapper implements PropertyMapper<ComboBox<String>, String> {

  @Override
  public String getPropertyFromJavaFx(ComboBox<String> javaFx) {
    return javaFx.getValue();
  }

  @Override
  public void mapPropertyToJavaFx(String modelProperty, ComboBox<String> javaFx) {
    javaFx.setValue(modelProperty);

  }

}
