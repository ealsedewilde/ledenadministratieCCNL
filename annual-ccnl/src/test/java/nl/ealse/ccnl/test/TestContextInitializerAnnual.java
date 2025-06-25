package nl.ealse.ccnl.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;

@Slf4j
public class TestContextInitializerAnnual implements ContextInitializer {
  
  boolean started;
  
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
      loadProperties("/excel.properties");
      entityManagerProvider = new TestEntityManagerProviderAnnual();
      loadPreferences();
      incassoProperties = initializeIncassoProperties(); 
      started = true;
    }
    
  }
  
  private void loadProperties(String location) {
    try (InputStream is = TestContextInitializerAnnual.class.getResourceAsStream(location)) {
      Properties props = new Properties();
      props.load(is);
      properties.putAll(props);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }

  }
  
  private DirectDebitConfig initializeIncassoProperties() {
    DirectDebitConfig config = new DirectDebitConfig();
    DDConfigStringEntry e = new DDConfigStringEntry();
  
    e.setValue("jaarlijks-CCNL-%s");
    e.setDescription(
        "Uniek kenmerk van de machtiging; %s markeert de plek voor lidmaatschapnummer");
    config.setAuthorization(e);
  
    e = new DDConfigStringEntry();
    e.setValue("RCUR");
    e.setDescription("Vast waarde; herhalende incasso");
    config.setAuthorizationType(e);
  
    int year = LocalDate.now().getYear();
    e = new DDConfigStringEntry();
    e.setValue(String.format("lidmaatschap %d-%d", year, year + 1));
    e.setDescription("Kenmerk op afschrift lid");
    config.setDirectDebitDescription(e);
  
    e = new DDConfigStringEntry();
    e.setValue("C:/temp");
    e.setDescription("Map voor INCASSO bestand");
    config.setDirectDebitDir(e);
  
    e = new DDConfigStringEntry();
    e.setValue("NL65ZZZ403419230000");
    e.setDescription("SEPA incassant id van de club");
    config.setDirectDebitId(e);
  
    e = new DDConfigStringEntry();
    e.setValue("NL97INGB0004160835");
    e.setDescription("Nummer waarop incasso binnenkomt");
    config.setIbanNumber(e);
  
    e = new DDConfigStringEntry();
    e.setValue("CITROEN CLUB NEDERLAND");
    e.setDescription("Naam op rekening incassant");
    config.setClubName(e);
  
    e = new DDConfigStringEntry();
    e.setValue(String.format("CCNL-lidmaatschap-%d-%d", year, year + 1));
    e.setDescription("Unieke identificatie in het incassobestand");
    // Zie ook https://www.ing.nl/zakelijk/betalen/geld-ontvangen/INCASSO/handleidingen/index.html
    config.setMessageId(e);
  
    DDConfigAmountEntry a = new DDConfigAmountEntry();
    config.setDirectDebitAmount(a);
    a.setValue(BigDecimal.valueOf(32.5));
    a.setDescription("Contributie bij Automatische Incasso");
  
    DDConfigDateEntry d = new DDConfigDateEntry();
    d.setValue(LocalDate.now().plusDays(6));
    d.setDescription("Datum waarop de incasso uitgevoerd moet worden");
    config.setDirectDebitDate(d);
  
    DDConfigBooleanEntry b = new DDConfigBooleanEntry();
    b.setValue(false);
    b.setDescription("Testbestand aanmaken?");
    config.setTestRun(b);
  
    return config;
  
  }



}
