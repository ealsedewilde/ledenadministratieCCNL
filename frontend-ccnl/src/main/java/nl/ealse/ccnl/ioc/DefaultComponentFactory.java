package nl.ealse.ccnl.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ConfigurationException;

/**
 * Simple 'Inversion Of Control' utility to provide a controller component. There are are number of
 * requirements for this utility to work correctly:
 * <ol>
 * <li>All components are created in the (single) application thread</li>
 * <li>All components have just one public constructor; component injection is done via this
 * constructor</li>
 * <li>All components are singletons</li>
 * </ol>
 */
@Slf4j
public class DefaultComponentFactory implements ComponentFactory {

  /**
   * The map with already constructed singleton components.
   */
  private final Map<Class<?>, Object> componentMap = new HashMap<>();

  /**
   * Look up a component.
   * 
   * @param <T> type of the component
   * @param clazz class of the component
   * @return the requested component
   */
  @SuppressWarnings("unchecked")
  public <T> T getComponent(Class<T> clazz) {
    Object component = componentMap.get(clazz);
    if (component == null) {
      component = newComponent(clazz);
      componentMap.put(clazz, component);
    }
    return (T) component;
  }

  private Object newComponent(Class<?> clazz) {
    Constructor<?> constructor = clazz.getConstructors()[0];
    Class<?>[] parmTypes = constructor.getParameterTypes();
    Object[] parms = new Object[parmTypes.length];
    for (int ix = 0; ix < parmTypes.length; ix++) {
      parms[ix] = getComponent(parmTypes[ix]);
    }
    try {
      return constructor.newInstance(parms);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      String message = String.format("Class %s could not be instantiated", clazz);
      log.error(message, e);
      throw new ConfigurationException(message, e);
    }
  }

}
