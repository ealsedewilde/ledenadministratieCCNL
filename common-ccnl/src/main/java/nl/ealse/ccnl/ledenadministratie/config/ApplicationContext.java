package nl.ealse.ccnl.ledenadministratie.config;

import java.util.Optional;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

/**
 * Context for application wide used functions.
 */
@UtilityClass
public class ApplicationContext {

  private static final ContextInitializer initializer;

  /**
   * There is one ContextInitializer implementation available for production. For unittests there
   * are implementations per Maven module.
   */
  static {
    ServiceLoader<ContextInitializer> serviceLoader = ServiceLoader.load(ContextInitializer.class);
    Optional<ContextInitializer> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      initializer = first.get();
    } else {
      throw new ExceptionInInitializerError("No ContextInitializer available");
    }
  }

  /**
   * Thread aware handler for an EntityManager.
   * 
   * @return
   */
  public EntityManagerProvider getEntityManagerProvider() {
    return initializer.getEntityManagerProvider();
  }

  /**
   * Get the actual value of a preference. The user might change a preference or might restore the
   * database. This must immediately be reflected in this getter.
   * 
   * @param key used to lookup
   * @return actual value
   */
  public String getPreference(String key) {
    return initializer.getPreferences().getProperty(key);
  }

  /**
   * Get the actual value of a preference.
   * 
   * @param key used to lookup
   * @param defaultValue value returned when not found
   * @return actual value or defaultValue
   */
  public String getPreference(String key, String defaultValue) {
    return initializer.getPreferences().getProperty(key, defaultValue);
  }

  /**
   * Get the value of a immutable property.
   * 
   * @param key
   * @return
   */
  public String getProperty(String key) {
    return initializer.getProperties().getProperty(key);
  }

  /**
   * Get the value of a immutable property.
   * 
   * @param key used to lookup
   * @param defaultValue value returned when not found
   * @return actual value or defaultValue
   */
  public String getProperty(String key, String defaultValue) {
    return initializer.getProperties().getProperty(key, defaultValue);
  }

  /**
   * Obtain ready to use singleton component. The component is constructed vai constructor
   * dependency injection.
   * 
   * @param <T>
   * @param clazz
   * @return
   */
  public <T> T getComponent(Class<T> clazz) {
    return initializer.getComponent(clazz);
  }

  /**
   * Load/Reload all preferences. Must be invoked at application, start, database restore or change
   * made by the user.
   */
  public void reloadPreferences() {
    initializer.reloadPreferences();
  }

  /**
   * Make the ApplicationContext available to the application.
   */
  public void start() {
    initializer.start();
  }
  
  /**
   * Close all database connections and stop the application.
   */
  public void stop() {
    EntityManagerProvider emp = getEntityManagerProvider();
    if (emp != null) {
      emp.shutdown();
    }
    System.exit(0);
  }

}
