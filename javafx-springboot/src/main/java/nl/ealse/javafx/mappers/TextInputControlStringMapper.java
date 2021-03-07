package nl.ealse.javafx.mappers;

import javafx.scene.control.TextInputControl;

public class TextInputControlStringMapper implements PropertyMapper<TextInputControl, String> {

  @Override
  public String getPropertyFromJavaFx(TextInputControl javaFx) {
    String text = javaFx.getText();
    if (text == null || text.trim().isEmpty()) {
      return null;
    }
    return text;
  }

  @Override
  public void mapPropertyToJavaFx(String modelProperty, TextInputControl javaFx) {
    javaFx.setText(modelProperty);
  }

}
