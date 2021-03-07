package nl.ealse.javafx.mapping;

import java.lang.reflect.Field;
import lombok.experimental.UtilityClass;

@UtilityClass
class FieldHandler {

  /**
   * Retrieve a field from a type hierarchy.
   * 
   * @param clazz - the class to examine
   * @param fieldName - the name of the field to retrieve
   * @return the desired field
   * @throws NoSuchFieldException - when required field is not found
   */
  Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Field field;
    try {
      field = clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      if (clazz.getSuperclass() != null) {
        field = getField(clazz.getSuperclass(), fieldName);
      } else {
        throw e;
      }
    }
    return field;
  }

}
