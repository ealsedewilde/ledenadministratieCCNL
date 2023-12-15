package nl.ealse.ccnl.ledenadministratie.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


@UtilityClass
@Slf4j
public class ApplicationProperties {
  
  private final Properties properties = new Properties();
  static {
    try (InputStream is = ApplicationProperties.class.getResourceAsStream("/application.properties")) {
      properties.load(is);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }
  }
  
  public String getProperty(String key) {
    return properties.getProperty(key);
  }
  
  
  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

}
