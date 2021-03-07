package nl.ealse.javafx.mappers;

import javafx.scene.control.ComboBox;

public class ComboBoxIntegerMapper implements PropertyMapper<ComboBox<Integer>, Integer> {

  @Override
  public Integer getPropertyFromJavaFx(ComboBox<Integer> javaFx) {
    return javaFx.getValue();
  }

  @Override
  public void mapPropertyToJavaFx(Integer modelProperty, ComboBox<Integer> javaFx) {
    javaFx.setValue(modelProperty);

  }

}
