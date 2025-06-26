package nl.ealse.ccnl.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

@Slf4j
public class TestContextInitializerExcel implements ContextInitializer {
  
  public TestContextInitializerExcel() {
    initialize();
  };

  @Override
  public EntityManagerProvider getEntityManagerProvider() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Properties getPreferences() {
    throw new UnsupportedOperationException();
  }
  
  @Getter
  private Properties properties = new Properties();

  @Override
  public <T> T getComponent(Class<T> clazz) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void reloadPreferences() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void start() {
    throw new UnsupportedOperationException();
  }

  private void initialize() {
      loadProperties("/excel.properties");
  }
  
  private void loadProperties(String location) {
    try (InputStream is = TestContextInitializerExcel.class.getResourceAsStream(location)) {
      Properties props = new Properties();
      props.load(is);
      properties.putAll(props);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }

  }


}
