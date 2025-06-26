package nl.ealse.ccnl.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

@Slf4j
public class TestContextInitializerCommon implements ContextInitializer {

  public TestContextInitializerCommon() {
    initialize();
  };

  @Getter
  private EntityManagerProvider entityManagerProvider;

  @Getter
  private Properties preferences = new Properties();

  @Getter
  private final Properties properties = new Properties();

  @Override
  public <T> T getComponent(Class<T> clazz) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void reloadPreferences() {
    preferences.put("ccnl.contributie.incasso", "€ 32,50");
    preferences.put("ccnl.contributie.overboeken", "€ 35,00");
  }

  @Override
  public void start() {
    throw new UnsupportedOperationException();
  }

  private void initialize() {
    loadProperties("/application.properties");
    entityManagerProvider = new TestEntityManagerProviderCommon(properties);
    reloadPreferences();
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
