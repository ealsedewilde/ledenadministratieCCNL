package nl.ealse.javafx.mappers;

import javafx.scene.control.Control;
import lombok.extern.slf4j.Slf4j;

/**
 * Non function placeholder mapper.
 * 
 * @author ealse
 *
 */
@Slf4j
public class DefaultMapper implements PropertyMapper<Control, Object> {

  @Override
  public Object getPropertyFromJavaFx(Control javaFx) {
    log.warn("No mapping for" + javaFx.getClass().getSimpleName());
    return null;
  }

  @Override
  public void mapPropertyToJavaFx(Object modelProperty, Control javaFx) {
    log.warn("No mapping for" + modelProperty.getClass().getSimpleName());
  }

}
