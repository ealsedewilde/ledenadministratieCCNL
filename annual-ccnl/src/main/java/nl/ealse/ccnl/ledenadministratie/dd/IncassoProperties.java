package nl.ealse.ccnl.ledenadministratie.dd;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.ConfigurationException;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;

/**
 * Properties needed for generation a DirectDebit file.
 * 
 * @author ealse
 *
 */
@Slf4j
@UtilityClass
public class IncassoProperties {
  
  @Getter
  private final DirectDebitConfig config;
  
  static {
    EntityManager em = ApplicationContext.getEntityManagerProvider().getEntityManager();
    DirectDebitConfig props = em.find(DirectDebitConfig.class, 1);
    if (props == null) {
      IncassoPropertiesProvider initializer = new IncassoPropertiesProvider(em);
      props = initializer.getIncassoConfig();
    }
    config = props;
  }

  public String getIbanNummer() {
    return config.getIbanNumber().getValue();
  }

  public String getIncassantId() {
    return config.getDirectDebitId().getValue();
  }

  public BigDecimal getIncassoBedrag() {
    return config.getDirectDebitAmount().getValue();
  }

  public LocalDate getIncassoDatum() {
    return config.getDirectDebitDate().getValue();
  }

  public String getIncassoReden() {
    return config.getDirectDebitDescription().getValue();
  }

  public String getMachtigingReferentie() {
    return config.getAuthorization().getValue();
  }

  public String getMachtigingType() {
    return config.getAuthorizationType().getValue();
  }

  public String getMessageId() {
    return config.getMessageId().getValue();
  }

  public String getNaam() {
    return config.getClubName().getValue();
  }

  public String getIncassoDirectory() {
    return config.getDirectDebitDir().getValue();
  }

  /**
   * 'Test' produces a file with a maximum of 10 transactions. Such a file can be used in a test run
   * at the bank.
   * 
   * @return <code>true</code> in geval van testrun
   */
  public boolean isTest() {
    return config.getTestRun().isValue();
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
      d.setValue(LocalDate.now().plusDays(8));
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
        log.error("Failed to persist incasso properties", ex);
        em.getTransaction().rollback();
        throw new ConfigurationException("Could not initialize configuration", ex);
      }
  
    }
  
  }


}
