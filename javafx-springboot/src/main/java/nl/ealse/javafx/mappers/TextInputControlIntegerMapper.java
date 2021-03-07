package nl.ealse.javafx.mappers;

import javafx.scene.control.TextInputControl;

public class TextInputControlIntegerMapper implements PropertyMapper<TextInputControl, Integer> {

  @Override
  public Integer getPropertyFromJavaFx(TextInputControl javaFx) {
    String text = javaFx.getText().trim();
    if (text == null || text.isEmpty()) {
      return null;
    }
    return Integer.valueOf(text);
  }

  @Override
  public void mapPropertyToJavaFx(Integer modelProperty, TextInputControl javaFx) {
    if (modelProperty != null) {
      javaFx.setText(modelProperty.toString());
    } else {
      javaFx.setText("0");
    }
  }

}
