package nl.ealse.javafx.mappers;

import javafx.scene.control.ChoiceBox;

public class ChoiceBoxStringMapper implements PropertyMapper<ChoiceBox<String>, String> {

  @Override
  public String getPropertyFromJavaFx(ChoiceBox<String> javaFx) {
    return javaFx.getValue();
  }

  @Override
  public void mapPropertyToJavaFx(String modelProperty, ChoiceBox<String> javaFx) {
    javaFx.setValue(modelProperty);

  }

}
