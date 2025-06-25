package nl.ealse.ccnl.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ioc.ComponentFactory;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;

@Slf4j
public class TestContextInitializerFrontend implements ContextInitializer {
  
  boolean started;
  
  private ComponentFactory componentFactory = new TestComponentFactory();

  @Getter
  private EntityManagerProvider entityManagerProvider; 

  
  @Getter
  private Properties preferences = new Properties();
  
  @Getter
  private Properties properties = new Properties();

  @Getter
  private DirectDebitConfig incassoProperties;

  @Override
  public <T> T getComponent(Class<T> clazz) {
    return componentFactory.getComponent(clazz);
  }

  @Override
  public void loadPreferences() {
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
    if (!started) {
      loadProperties("/application.properties");
      loadProperties("/excel.properties");
      entityManagerProvider = new TestEntityManagerProviderFrontend();
      loadPreferences();
      incassoProperties = initializeIncassoProperties(); 
      started = true;
    }
    
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
  
  private DirectDebitConfig initializeIncassoProperties() {
    DirectDebitConfig config = new DirectDebitConfig();
    
    DDConfigDateEntry d = new DDConfigDateEntry();
    d.setValue(LocalDate.now().withMonth(4));
    d.setDescription("Datum waarop de incasso uitgevoerd moet worden");
    config.setDirectDebitDate(d);
    
    
    DDConfigAmountEntry a = new DDConfigAmountEntry();
    config.setDirectDebitAmount(a);
    a.setValue(BigDecimal.valueOf(32.5));
    a.setDescription("Contributie bij Automatische Incasso");

    return config;
  }


}
