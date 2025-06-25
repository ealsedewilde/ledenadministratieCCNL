package nl.ealse.ccnl;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ioc.ComponentFactory;
import nl.ealse.ccnl.ioc.DefaultComponentFactory;
import nl.ealse.ccnl.ledenadministratie.config.ConfigurationException;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.DefaultEntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

@Slf4j
public class DefaultContextInitializer implements ContextInitializer {

  @Getter
  private final Properties properties = new Properties();

  @Getter
  private Properties preferences = new Properties();

  private final ComponentFactory componentFactory = new DefaultComponentFactory();

  public <T> T getComponent(Class<T> clazz) {
    return componentFactory.getComponent(clazz);
  }

  private DirectDebitConfig incassoProperties;

  public DirectDebitConfig getIncassoProperties() {
    // can't load during start because EntityManager not available yet.
    if (incassoProperties == null) {
      IncassoPropertiesProvider incassoPropertiesProvider =
          new IncassoPropertiesProvider(entityManagerProvider.getEntityManager());
      incassoProperties = incassoPropertiesProvider.getIncassoConfig();
    }
    return incassoProperties;
  }

  @Getter
  private EntityManagerProvider entityManagerProvider;

  private void loadProperties(String location) {
    try (InputStream is = DefaultContextInitializer.class.getResourceAsStream(location)) {
      Properties props = new Properties();
      props.load(is);
      properties.putAll(props);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }

  }

  public void loadPreferences() {
    SettingRepository dao = new SettingRepository();
    List<Setting> settings = dao.findByOrderBySettingsGroupAscKeyAsc();
    settings.forEach(setting -> preferences.put(setting.getId(), setting.getValue()));
  }

  public void start() {
    new TaskExecutor().execute(() -> {
      loadProperties("/application.properties");
      entityManagerProvider = new DefaultEntityManagerProvider();
      loadPreferences();
    });
    // need a plain Executor becaus a TaskExecutor cleans up an EntityManager which is not there yet
    Executors.newSingleThreadExecutor().execute(() -> loadProperties("/excel.properties"));

  }


  private static class IncassoPropertiesProvider {
    private final EntityManager em;

    public IncassoPropertiesProvider(EntityManager em) {
      this.em = em;
    }

    public DirectDebitConfig getIncassoConfig() {
      DirectDebitConfig config = em.find(DirectDebitConfig.class, 1);
      if (config == null) {
        config = initializeConfig();
        persistConfig(config);
      }
      return config;
    }


    private DirectDebitConfig initializeConfig() {
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

    private void persistConfig(DirectDebitConfig config) {
      em.getTransaction().begin();
      try {
        em.persist(config);
        em.getTransaction().commit();
      } catch (Exception ex) {
        log.error("Failed to persist incaaso properties", ex);
        em.getTransaction().rollback();
        throw new ConfigurationException("Could not initialize configuration", ex);
      }

    }

  }


}
