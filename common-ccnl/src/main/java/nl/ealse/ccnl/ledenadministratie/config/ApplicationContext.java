package nl.ealse.ccnl.ledenadministratie.config;

import java.util.Optional;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;

@UtilityClass
public class ApplicationContext {
  
  private static final ContextInitializer initializer;
  
  static {
    ServiceLoader<ContextInitializer> serviceLoader =
        ServiceLoader.load(ContextInitializer.class);
    Optional<ContextInitializer> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      initializer = first.get();
    } else {
      throw new ExceptionInInitializerError("No ContextInitializer available");
    }
  }
  
  public EntityManagerProvider getEntityManagerProvider() {
    return initializer.getEntityManagerProvider();
  }
  
  public DirectDebitConfig getIncassoProperties() {
    return initializer.getIncassoProperties();
  }
  
  public String getPreference(String key) {
    return initializer.getPreferences().getProperty(key);
  }
  
  public String getPreference(String key, String defaultValue) {
    return initializer.getPreferences().getProperty(key, defaultValue);
  }
  
  public String getProperty(String key) {
    return initializer.getProperties().getProperty(key);
  }
  
  public String getProperty(String key, String defaultValue) {
    return initializer.getProperties().getProperty(key, defaultValue);
  }
  
  public <T> T getComponent(Class<T> clazz) {
    return initializer.getComponent(clazz);
  }
  
  public void reloadPreferences() {
    initializer.loadPreferences();
  }
  
  public void start() {
    initializer.start();
  }

}
