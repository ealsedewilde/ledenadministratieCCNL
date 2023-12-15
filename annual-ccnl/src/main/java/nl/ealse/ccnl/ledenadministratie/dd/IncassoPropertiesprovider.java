package nl.ealse.ccnl.ledenadministratie.dd;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;
import nl.ealse.ccnl.ledenadministratie.util.EntityManagerProvider;

@Slf4j
public class IncassoPropertiesprovider {

  private final EntityManager em = EntityManagerProvider.getEntityManager();
  
  public DirectDebitConfig getProperties() {
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
    a.setValue(BigDecimal.valueOf(27.5));
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
