package nl.ealse.javafx.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Control;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperRegistry {

  /**
   * All registered standard {@link PropertyMapper}s.
   */
  private final Map<String, PropertyMapper<Control, Object>> mappers = new HashMap<>();

  /**
   * The complete set of standard {@link PropertyMapper}s.
   */
  static {
    mappers.put("CheckBoxboolean", handle(new CheckBoxMapper()));
    mappers.put("CheckBoxBoolean", handle(new CheckBoxMapper()));
    mappers.put("DatePickerLocalDate", handle(new DatePickerMapper()));
    mappers.put("ChoiceBoxString", handle(new ChoiceBoxStringMapper()));
    mappers.put("ComboBoxString", handle(new ComboBoxStringMapper()));
    mappers.put("ComboBoxInteger", handle(new ComboBoxIntegerMapper()));
    mappers.put("LabelString", handle(new LabelStringMapper()));
    mappers.put("LabelInteger", handle(new LabelIntegerMapper()));
    mappers.put("TextFieldBigDecimal", handle(new TextInputControlBigDecimalMapper()));
    mappers.put("TextFieldString", handle(new TextInputControlStringMapper()));
    mappers.put("TextFieldInteger", handle(new TextInputControlIntegerMapper()));
    mappers.put("TextFieldLocalDate", handle(new TextInputControlLocalDateMapper()));
    mappers.put("TextAreaBigDecimal", handle(new TextInputControlBigDecimalMapper()));
    mappers.put("TextAreaString", handle(new TextInputControlStringMapper()));
    mappers.put("TextAreaInteger", handle(new TextInputControlIntegerMapper()));
  }

  @SuppressWarnings("unchecked")
  private PropertyMapper<Control, Object> handle(PropertyMapper<? extends Control, ?> p) {
    return (PropertyMapper<Control, Object>) p;
  }
  

  /**
   * Add an additional standard {@link PropertyMapper}.
   * 
   * @param propertyMapper - the {@link PropertyMapper} to register
   * @param javaFxClass - class to link with the {@link PropertyMapper}
   * @param modelClass - class to link with the {@link PropertyMapper}
   */
  public void registerPropertyMapper(
      PropertyMapper<? extends Object, ? extends Object> propertyMapper, Class<?> javaFxClass,
      Class<?> modelClass) {
    String key = javaFxClass.getSimpleName() + modelClass.getSimpleName();
    mappers.put(key, handle(propertyMapper));
  }
  
  public Optional<PropertyMapper<Control, Object>> getPropertyMapper(String key) {
    return Optional.ofNullable(mappers.get(key));
  }


}
