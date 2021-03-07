package nl.ealse.javafx.mappers;

import javafx.scene.control.Label;

public class LabelStringMapper implements PropertyMapper<Label, String> {

  @Override
  public String getPropertyFromJavaFx(Label javaFx) {
    String text = javaFx.getText();
    if (text.isEmpty()) {
      return null;
    }
    return text;
  }

  @Override
  public void mapPropertyToJavaFx(String modelProperty, Label javaFx) {
    javaFx.setText(modelProperty);

  }

}
