package nl.ealse.ccnl.control;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Format a date in a TableColumn.
 *
 * @author ealse
 *
 */
@Slf4j
public class DatePropertyValueFactory
    implements Callback<TableColumn.CellDataFeatures<?, String>, ObservableValue<String>> {

  private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Getter
  @Setter
  private String property;

  private Method getter;

  @Override
  public ObservableValue<String> call(CellDataFeatures<?, String> param) {
    if (getter == null) {
      String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
      try {
        getter = param.getValue().getClass().getMethod(methodName);
      } catch (Exception e) {
        log.warn("Error retrieving property getter method", e);
      }
    }
    return retrieveProperty(param);
  }

  private ObservableValue<String> retrieveProperty(CellDataFeatures<?, String> param) {
    try {
      LocalDate date = (LocalDate) getter.invoke(param.getValue());
      return new SimpleStringProperty(date.format(DF));
    } catch (Exception e) {
      log.warn("Error retrieving property", e);
    }
    return null;
  }

}
