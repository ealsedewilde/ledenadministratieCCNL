package nl.ealse.ccnl.ledenadministratie.config;

import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DatabaseProperties {
  
  private Properties properties;

  public String getProperty(String key) {
    if (properties == null) {
      initialize();
    }
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    if (properties == null) {
      initialize();
    }
    return properties.getProperty(key, defaultValue);
  }
  
  public void reload() {
    initialize();
  }

  public synchronized void initialize() {
    ServiceLoader<DatabasePropertiesProvider> serviceLoader =
        ServiceLoader.load(DatabasePropertiesProvider.class);
    Optional<DatabasePropertiesProvider> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      properties = first.get().getProperties();
    } else {
      throw new ExceptionInInitializerError("No DatabasePropertiesProvider available");
    }
  }
}
