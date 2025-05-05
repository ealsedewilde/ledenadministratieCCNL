package nl.ealse.ccnl.ioc;

/**
 * Simple 'Inversion Of Control' utility to provide a controller component.
 * There are are number of requirements for this utility to work correctly:
 * <ol>
 * <li>All components are created in the (single) application thread</li>
 * <li>All components have just one public constructor; component injection is done via this constructor</li>
 * <li>All components are singletons</li>
 * </ol>
 */
public interface ComponentFactory {
  
  /**
   * Look up a controller.
   * @param <T> type of the controller
   * @param clazz class of the controller
   * @return the requested controller
   */
  public <T> T getComponent(Class<T> clazz);


}
