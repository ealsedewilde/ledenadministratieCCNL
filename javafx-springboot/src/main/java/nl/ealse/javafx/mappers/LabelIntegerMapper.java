package nl.ealse.javafx.mappers;

import javafx.scene.control.Label;

public class LabelIntegerMapper implements PropertyMapper<Label, Integer> {

  @Override
  public Integer getPropertyFromJavaFx(Label javaFx) {
    String text = javaFx.getText();
    if (text.isEmpty()) {
      return null;
    }
    return Integer.valueOf(text);
  }

  @Override
  public void mapPropertyToJavaFx(Integer modelProperty, Label javaFx) {
    if (modelProperty != null) {
      javaFx.setText(modelProperty.toString());
    } else {
      javaFx.setText("0");
    }

  }

}
