package nl.ealse.ccnl.ledenadministratie.config;

import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DatabaseProperties {
  
  private static final DatabasePropertiesProvider provider;
  
  static {
    ServiceLoader<DatabasePropertiesProvider> serviceLoader =
        ServiceLoader.load(DatabasePropertiesProvider.class);
    Optional<DatabasePropertiesProvider> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      provider = first.get();
    } else {
      throw new ExceptionInInitializerError("No DatabasePropertiesProvider available");
    }
  }
  
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
  
  public synchronized void initialize() {
    properties = provider.getProperties();
  }
}
