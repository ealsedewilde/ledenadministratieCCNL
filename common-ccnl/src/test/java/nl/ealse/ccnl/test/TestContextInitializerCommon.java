package nl.ealse.ccnl.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;

@Slf4j
public class TestContextInitializerCommon implements ContextInitializer {
  
  boolean started;
  
  @Getter
  private EntityManagerProvider entityManagerProvider; 
  
  @Getter
  private Properties preferences = new Properties();
  
  @Getter
  private final Properties properties = new Properties();

  @Override
  public DirectDebitConfig getIncassoProperties() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T getComponent(Class<T> clazz) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void loadPreferences() {
    preferences.put("ccnl.contributie.incasso", "€ 32,50");
    preferences.put("ccnl.contributie.overboeken", "€ 35,00");
  }

  @Override
  public void start() {
    if (!started) {
      loadProperties("/application.properties");
      entityManagerProvider = new TestEntityManagerProviderCommon();
      loadPreferences();
      started = true;
    }
  }
  
  
  private void loadProperties(String location) {
    try (InputStream is = TestContextInitializerCommon.class.getResourceAsStream(location)) {
      Properties props = new Properties();
      props.load(is);
      properties.putAll(props);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }

  }


}
