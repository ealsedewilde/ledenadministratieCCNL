package nl.ealse.ccnl.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ioc.ComponentFactory;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

@Slf4j
public class TestContextInitializerFrontend implements ContextInitializer {

  public TestContextInitializerFrontend() {
    initialize();
  }

  private ComponentFactory componentFactory = new TestComponentFactory();

  @Getter
  private EntityManagerProvider entityManagerProvider;


  @Getter
  private Properties preferences = new Properties();

  @Getter
  private Properties properties = new Properties();

  @Override
  public <T> T getComponent(Class<T> clazz) {
    return componentFactory.getComponent(clazz);
  }

  @Override
  public void reloadPreferences() {
    preferences.put("ccnl.mail.host", "localhost");
    preferences.put("ccnl.mail.port", "25");
    preferences.put("ccnl.mail.properties.mail.smtp.auth", "false");
    preferences.put("ccnl.mail.properties.mail.smtp.starttls.enable", "false");
    preferences.put("ccnl.mail.subject", "test mail");

    preferences.put("ccnl.contributie.incasso", "32,50");
    preferences.put("ccnl.contributie.overboeken", "35,00");

  }

  @Override
  public void start() {
    throw new UnsupportedOperationException();
  }

  private void initialize() {
    loadProperties("/application.properties");
    loadProperties("/excel.properties");
    entityManagerProvider = new TestEntityManagerProviderFrontend(properties);
    reloadPreferences();
  }

  private void loadProperties(String location) {
    try (InputStream is = TestContextInitializerFrontend.class.getResourceAsStream(location)) {
      Properties props = new Properties();
      props.load(is);
      properties.putAll(props);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }

  }

}
