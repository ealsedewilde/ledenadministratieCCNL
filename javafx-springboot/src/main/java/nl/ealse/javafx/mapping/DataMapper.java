package nl.ealse.javafx.mapping;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeMap;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.mappers.MapperRegistry;
import nl.ealse.javafx.mappers.PropertyMapper;
import nl.ealse.javafx.mapping.BeanExplorer.PropertyContext;

/**
 * Map data from View to Model and vice versa.
 * 
 * @author ealse
 */
@UtilityClass
@Slf4j
public final class DataMapper {

  private static final String REFLECTION_ERROR = "Reflection error";

  /**
   * Mapping from the FXML-form to the model.
   * 
   * @param <T> the type of the model
   * @param form - source
   * @param model - target
   * @return - the updated model
   */
  public <T> T formToModel(Object form, T model) {
    Map<String, PropertyContext> formProperties = new BeanExplorer(form).describeBean();
    Map<String, PropertyContext> beanProperties = new BeanExplorer(model).describeBean();
    FormToModelUtil<T> formToModelUtil = new FormToModelUtil<T>(model, beanProperties);
    try {
      for (PropertyContext formProperty : formProperties.values()) {
        PropertyContext beanProperty = beanProperties.get(formProperty.getName());
        beanProperties = formToModelUtil.mapFormPropertyOnModelProperty(beanProperty, formProperty);
      }
      return model;
    } catch (IllegalArgumentException | SecurityException e) {
      log.error(REFLECTION_ERROR, e);
      throw new MappingException(REFLECTION_ERROR, e);
    }
  }

  /**
   * Mapping from model to the FXML-form.
   * 
   * @param model - source
   * @param form - target
   */
  public void modelToForm(Object form, Object model) {
    Map<String, PropertyContext> formProperties = new BeanExplorer(form).describeBean();
    Map<String, PropertyContext> beanProperties = new BeanExplorer(model).describeBean();
    try {
      for (PropertyContext formProperty : formProperties.values()) {
        if (formProperty.getBean() == null) {
          throw new MappingException(String.format("Property %s is not available in the form. "
              + "(Perhaps the form is not initialized yet.)", formProperty.getName()));
        }

        log.debug(formProperty.getName());
        PropertyContext beanProperty = beanProperties.get(formProperty.getName());

        if (beanProperty == null || beanProperty.getBean() == null) {
          log.warn(String.format("Property %s in the model is null", formProperty.getName()));
          return;
        }
        Method modelReadmethod = beanProperty.getPropertyDescriptor().getReadMethod();

        PropertyDescriptor property = formProperty.getPropertyDescriptor();
        Method formReadmethod = property.getReadMethod();
        Control javaFx = (Control) formReadmethod.invoke(formProperty.getBean());
        if (javaFx != null) {
          Class<?> formClass = formProperty.getBean().getClass();
          Class<?> modelPropertyClass = beanProperty.getPropertyDescriptor().getPropertyType();
          Optional<PropertyMapper<Control, Object>> propertyMapper =
              findPropertyMapper(formClass, property, javaFx.getClass(), modelPropertyClass);
          if (propertyMapper.isPresent()) {
            Object propertyValueInModel = modelReadmethod.invoke(beanProperty.getBean());
            propertyMapper.get().mapPropertyToJavaFx(propertyValueInModel, javaFx);
          }
        }
      }
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      log.error(REFLECTION_ERROR, e);
      throw new MappingException(REFLECTION_ERROR, e);
    }
  }

  /**
   * Retrieve the {@link PropertyMapper} for a property.
   * 
   * @param formClass - name of the javafx form/controller
   * @param property - name of the property
   * @param javaFxClass - type of the javafx control in the form
   * @param modelPropertyClass - type of the property in the model
   * @return
   */
  private Optional<PropertyMapper<Control, Object>> findPropertyMapper(Class<?> formClass,
      PropertyDescriptor property, Class<?> javaFxClass, Class<?> modelPropertyClass) {
    Optional<PropertyMapper<Control, Object>> optionalMapper =
        getCustomPropertyMapper(formClass, property.getName());
    if (!optionalMapper.isEmpty()) {
      // Return annotated custom mapper
      return optionalMapper;
    }
    // Try a Standard Mapper
    String key = javaFxClass.getSimpleName() + modelPropertyClass.getSimpleName();
    return MapperRegistry.getPropertyMapper(key);
  }

  /**
   * Get the custom {@link PropertyMapper}.
   * 
   * @param clazz - containing the field to map
   * @param fieldName - name of the field to map
   * @return
   */
  private Optional<PropertyMapper<Control, Object>> getCustomPropertyMapper(Class<?> clazz,
      String fieldName) {
    try {
      Field field = FieldHandler.getField(clazz, fieldName);
      Mapping mapping = field.getAnnotation(Mapping.class);
      if (mapping != null && !mapping.propertyMapper().isInterface()) {
        Object instance = mapping.propertyMapper().getConstructors()[0].newInstance();
        @SuppressWarnings("unchecked")
        PropertyMapper<Control, Object> propertyMapper = (PropertyMapper<Control, Object>) instance;
        return Optional.of(propertyMapper);
      }
      return Optional.empty();
    } catch (SecurityException | InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
      log.error(REFLECTION_ERROR, e);
      throw new MappingException(REFLECTION_ERROR, e);
    }

  }

  /**
   * Test method.
   * <p>
   * Mapping is based on name equality of properties in both the form and the model. The properties
   * in the form are leading. It's very easy to have mistakes, like defining a getter method on a
   * {@link Label} which should not be mapped on the model.
   * </p>
   * This method helps finding mapping errors during development phase.
   * 
   * @param form - input for the explain report
   * @param model - to relate to the form
   */
  public String explain(Object form, Object model) {
    StringJoiner sj = new StringJoiner("\n");
    Map<String, PropertyContext> formProperties = new TreeMap<>();
    formProperties.putAll(new BeanExplorer(form).describeBean());
    Map<String, PropertyContext> beanProperties = new BeanExplorer(model).describeBean();
    try {
      for (Entry<String, PropertyContext> entry : formProperties.entrySet()) {
        String propertyName = entry.getKey();
        PropertyContext beanProperty = beanProperties.get(propertyName);
        if (beanProperty == null) {
          sj.add(String.format("[WARN] Property %s doesn't exist in the model", propertyName));
        } else {
          PropertyContext formProperty = entry.getValue();
          Object formBean = formProperty.getBean();
          PropertyDescriptor formPropertyDescriptor = formProperty.getPropertyDescriptor();
          PropertyDescriptor beanPropertyDescriptor = beanProperty.getPropertyDescriptor();

          explainFormReadMethod(sj, propertyName, formBean, formPropertyDescriptor,
              beanPropertyDescriptor);
        }

      }
    } catch (Exception e) {
      log.error("Error while analyzing mapping", e);
    }
    return sj.toString();
  }

  private void explainFormReadMethod(StringJoiner sj, String propertyName, Object formBean,
      PropertyDescriptor formPropertyDescriptor, PropertyDescriptor beanPropertyDescriptor) {
    Method formReadmethod = formPropertyDescriptor.getReadMethod();
    if (formReadmethod != null) {
      Class<?> formClass = formBean.getClass();
      Class<?> javaFxClass = formReadmethod.getReturnType();
      Optional<PropertyMapper<Control, Object>> propertyMapper = findPropertyMapper(formClass,
          formPropertyDescriptor, javaFxClass, beanPropertyDescriptor.getPropertyType());
      if (propertyMapper.isPresent()) {
        sj.add(String.format("[INFO] Property %s \t uses PropertyMapper %s", propertyName,
            propertyMapper.get().getClass().getName()));
      } else {
        sj.add(String.format("[WARN] Property %s has no PropertyMapper available", propertyName));
      }
      if (formPropertyDescriptor.getWriteMethod() == null) {
        sj.add(String.format("[WARN] No setter method in the form for property %s", propertyName));
      }
      if (beanPropertyDescriptor.getReadMethod() == null) {
        sj.add(String.format("[WARN] No getter method in the model for property %s", propertyName));
      }
      if (beanPropertyDescriptor.getWriteMethod() == null) {
        sj.add(String.format("[WARN] No setter method in the model for property %s", propertyName));
      }
    } else if (beanPropertyDescriptor.getReadMethod() != null) {
      sj.add(String.format("[WARN] No mapping for property %s", propertyName));
    }
  }

  /**
   * Utility to provide the property target bean in the model
   * 
   * @author ealse
   *
   * @param <T>
   */
  private static class FormToModelUtil<T> {
    private final T model;
    private Map<String, PropertyContext> beanProperties;

    private FormToModelUtil(T model, Map<String, PropertyContext> beanProperties) {
      this.model = model;
      this.beanProperties = beanProperties;
    }

    /**
     * Write the content of a javafx control to the model. When needed, the parent bean of the bean
     * property will be instantiated in the model. In that case the beanProperties will be updated.
     * 
     * @param beanProperty - target
     * @param formProperty - source
     * @return - the potentially updated beanProperties
     */
    private Map<String, PropertyContext> mapFormPropertyOnModelProperty(
        PropertyContext beanProperty, PropertyContext formProperty) {
      PropertyDescriptor formPropertyDescriptor = formProperty.getPropertyDescriptor();
      Method formReadmethod = formPropertyDescriptor.getReadMethod();
      try {
        Control javaFx = (Control) formReadmethod.invoke(formProperty.getBean());
        if (javaFx != null) {
          Class<?> formClass = formProperty.getBean().getClass();
          PropertyDescriptor beanPropertyDescriptor = beanProperty.getPropertyDescriptor();
          Method modelWritemethod = beanPropertyDescriptor.getWriteMethod();
          if (modelWritemethod == null) {
            throw new MappingException(
                "No write method in model for " + formPropertyDescriptor.getName());
          } else {
            Optional<PropertyMapper<Control, Object>> propertyMapper =
                findPropertyMapper(formClass, formPropertyDescriptor, javaFx.getClass(),
                    beanPropertyDescriptor.getPropertyType());
            if (propertyMapper.isPresent()) {
              Object targetBean = provideTargetBean(beanProperty);
              // Add the targetValue to the model
              Object targetPropertyValue = propertyMapper.get().getPropertyFromJavaFx(javaFx);
              modelWritemethod.invoke(targetBean, targetPropertyValue);
            }
          }
        }
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
          | SecurityException e) {
        log.error(REFLECTION_ERROR, e);
        throw new MappingException(REFLECTION_ERROR, e);
      }
      return beanProperties;
    }

    /**
     * Provide the target bean for the beanProperty.
     * 
     * @param beanProperty - context for the target to provide
     * @return requested target for the beanProperty
     */
    private Object provideTargetBean(PropertyContext beanProperty) {
      PropertyDescriptor beanPropertyDescriptor = beanProperty.getPropertyDescriptor();
      Object target = beanProperty.getBean();
      if (target == null) {
        Class<?> targetClass = beanPropertyDescriptor.getWriteMethod().getDeclaringClass();
        try {
          target = targetClass.getConstructor().newInstance();
          Object targetParent = beanProperty.getParentBean();
          if (targetParent != null) {
            addTargetToModel(targetParent, target);
            // Refresh so other properties can benefit from the added bean
            beanProperties = new BeanExplorer(model).describeBean();
          } else {
            log.warn(String.format("incomplete model for %s; value not added to the model!",
                beanProperty.getName()));
            // The modelWritemethod will be invoked on a target that is not part of the
            // model
          }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
          log.error(REFLECTION_ERROR, e);
          throw new MappingException(REFLECTION_ERROR, e);
        }
      }
      return target;
    }

    /**
     * Add a bean property's parent to the model.
     * 
     * @param targetParent - the target bean for the model bean property
     * @param target - the model bean property
     * @param name - name of the bean property
     */
    private void addTargetToModel(Object targetParent, Object target) {
      try {
        BeanInfo beanInfo = Introspector.getBeanInfo(targetParent.getClass());
        for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
          Class<?>[] types = property.getWriteMethod().getParameterTypes();
          if (types.length == 1 &&  types[0].equals(target.getClass())) {
            property.getWriteMethod().invoke(targetParent, target);
            return;
          }
        }
      } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e) {
        log.error(REFLECTION_ERROR, e);
        throw new MappingException(REFLECTION_ERROR, e);
      }

    }

  }

}
