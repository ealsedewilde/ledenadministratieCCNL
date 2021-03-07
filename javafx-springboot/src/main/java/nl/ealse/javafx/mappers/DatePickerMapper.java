package nl.ealse.javafx.mappers;

import java.time.LocalDate;
import javafx.scene.control.DatePicker;

public class DatePickerMapper implements PropertyMapper<DatePicker, LocalDate> {

  @Override
  public LocalDate getPropertyFromJavaFx(DatePicker javaFx) {
    return javaFx.getValue();
  }

  @Override
  public void mapPropertyToJavaFx(LocalDate modelProperty, DatePicker javaFx) {
    javaFx.setValue(modelProperty);


  }

}
