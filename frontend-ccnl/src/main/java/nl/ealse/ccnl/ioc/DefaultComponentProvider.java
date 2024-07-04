package nl.ealse.ccnl.ioc;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple 'Inversion Of Control' utility to provide a controller component.
 * There are are number of requirements for this utility to work correctly:
 * <ol>
 * <li>All components are created in the (single) application thread</li>
 * <li>All components have just one public constructor; component injection is done via this constructor</li>
 * <li>All components are singletons</li>
 * </ol>
 */
@Slf4j
public class DefaultComponentProvider implements ComponentProvider {
  
  private final Map<Class<?>, WeakReference<?>> componentMap = new HashMap<>();
  
  /**
   * Look up a component.
   * @param <T> type of the component
   * @param clazz class of the component
   * @return the requested component
   */
  @SuppressWarnings("unchecked")
  public <T> T getComponent(Class<T> clazz) {
    WeakReference<?> reference = componentMap.get(clazz);
    if (reference == null || reference.get() == null) {
      reference = new WeakReference<>(newComponent(clazz));
      componentMap.put(clazz,  reference);
    }
    return (T) reference.get();
  }

  private Object newComponent(Class<?> clazz) {
    Constructor<?> constructor = clazz.getConstructors()[0];
    Class<?>[] parmTypes = constructor.getParameterTypes();
    Object[] parms = new Object[parmTypes.length];
    for (int ix = 0 ; ix < parmTypes.length ; ix++) {
      parms[ix] = getComponent(parmTypes[ix]);
    }
    try {
      return constructor.newInstance(parms);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      log.error(String.format("Class %s could not be instantiated", clazz), e);
      throw new ComponentProviderException(e);
    }
  }

  

}
