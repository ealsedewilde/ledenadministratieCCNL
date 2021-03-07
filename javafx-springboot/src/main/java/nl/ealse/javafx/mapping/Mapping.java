package nl.ealse.javafx.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.ealse.javafx.mappers.PropertyMapper;

/**
 * Optional annotation on a javafx control to steer the data mapping process.
 * 
 * @author ealse
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Mapping {

  /**
   * Ignore this javafx control in the data mapping.
   * 
   * @return
   */
  public boolean ignore() default false;

  /**
   * Link a specific {@link PropertyMapper} to this javafx control. Use this option in case where on
   * of the standard PropertyMappers can't handle this javafx control.
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Class<? extends PropertyMapper> propertyMapper() default PropertyMapper.class;

}
