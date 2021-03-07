package nl.ealse.javafx.mapping;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Retrieve all information on a Java bean that is needed for the {@link DataMapper}.
 * 
 * @author ealse
 *
 */
@Slf4j
class BeanExplorer {

  private static final String REFLECTION_ERROR = "Reflection error";

  /**
   * This Map will contain the result of the Java bean exploration. The key is the name of the
   * property the value is the description of the property.
   */
  @Getter
  private final Map<String, PropertyContext> propertyInfoMap = new HashMap<>();

  /**
   * The Java bean to explore.
   */
  private final Object bean;

  /**
   * Construct an instance aound a bean.
   * @param bean - The Java bean to explore.
   */
  BeanExplorer(Object bean) {
    this.bean = bean;
  }

  /**
   * Make a description of the Java bean.
   * 
   * @return - the description of the bean
   */
  public Map<String, PropertyContext> describeBean() {
    examineBean(bean, null, bean.getClass());
    return propertyInfoMap;
  }

  /**
   * Examine a Java bean can be an iterative process. A bean may contain child Java bean which must
   * be examined as well.
   * 
   * @param theBean - the optional bean instance to examine
   * @param parentBean - the optional parent of the bean
   * @param clazz - of the bean
   */
  private void examineBean(Object theBean, Object parentBean, Class<?> clazz) {
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
      for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
        Method readMethod = property.getReadMethod();
        if (readMethod != null) {
          examineBeanProperty(theBean, parentBean, clazz, property);
        }
      }
    } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | SecurityException e) {
      log.error(REFLECTION_ERROR, e);
      throw new MappingException(REFLECTION_ERROR, e);
    }
  }

  /**
   * Examine a property of the Java bean that is currently examined.
   * 
   * @param theBean - the bean to examine; potentially <tt>null</tt>
   * @param parentBean - parent of aBean; potentially <tt>null</tt>
   * @param clazz - of aBean
   * @param property - description of the property
   * @throws IllegalAccessException - n/a
   * @throws InvocationTargetException - n/a
   */
  private void examineBeanProperty(Object theBean, Object parentBean, Class<?> clazz,
      PropertyDescriptor property) throws IllegalAccessException, InvocationTargetException {
    String name = property.getName();
    Class<?> type = property.getPropertyType();
    if (isIncludeBeanProperty(clazz, name)) {
      if (isBeanToExplore(type)) {
        if (theBean == null) {
          examineBean(null, null, type);
        } else {
          Object childBean = property.getReadMethod().invoke(theBean);
          examineBean(childBean, theBean, type);
        }
      } else if (!"java.lang.Class".contentEquals(type.getName())) {
        PropertyContext pi = new PropertyContext(name, theBean, parentBean, property);
        propertyInfoMap.put(name, pi);
      }
    }
  }

  /**
   * Determine if the property is a Java bean that has to be explored.
   * 
   * @param type - property type
   * @return
   */
  private boolean isBeanToExplore(Class<?> type) {
    if (type.getPackageName().startsWith("java")) {
      return false;
    }
    return !type.isEnum();
  }

  /**
   * Should be include the bean property. A form property annotated with '@Mapping(ignore = true)'
   * will be skipped.
   * 
   * @param clazz - class to investigate
   * @param fieldName - of prorty in class
   * @return true if the property is to be included
   */
  private boolean isIncludeBeanProperty(Class<?> clazz, String fieldName) {
    try {
      Field field = FieldHandler.getField(clazz, fieldName);
      Mapping mapping = field.getAnnotation(Mapping.class);
      if (mapping != null) {
        return !mapping.ignore();
      }
      return true;
    } catch (SecurityException e) {
      log.error(REFLECTION_ERROR, e);
      throw new MappingException(REFLECTION_ERROR, e);
    } catch (NoSuchFieldException e) {
      return false;
    }
  }

  /**
   * Container for all relevant Bean property info.
   * 
   * @author ealse
   *
   */
  @AllArgsConstructor
  @Data
  public static class PropertyContext {

    /**
     * The name of the property.
     */
    private final String name;

    /**
     * The bean that contains the property.
     */
    private Object bean;

    /**
     * the parent of the bean that contains the property. This can be <tt>null</tt>.
     */
    private final Object parentBean;

    /**
     * Description of the property.
     */
    private final PropertyDescriptor propertyDescriptor;
  }
}
