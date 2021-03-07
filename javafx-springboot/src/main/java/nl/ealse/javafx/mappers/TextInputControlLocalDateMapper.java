package nl.ealse.javafx.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextInputControl;

public class TextInputControlLocalDateMapper
    implements PropertyMapper<TextInputControl, LocalDate> {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Override
  public LocalDate getPropertyFromJavaFx(TextInputControl javaFx) {
    String text = javaFx.getText();
    if (text.isEmpty()) {
      return null;
    }
    return LocalDate.parse(text, formatter);
  }

  @Override
  public void mapPropertyToJavaFx(LocalDate modelProperty, TextInputControl javaFx) {
    if (modelProperty != null) {
      javaFx.setText(modelProperty.format(formatter));
    }
  }

}
