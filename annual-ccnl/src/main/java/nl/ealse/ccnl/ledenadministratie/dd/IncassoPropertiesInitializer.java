package nl.ealse.ccnl.ledenadministratie.dd;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigAmountEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigBooleanEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigStringEntry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IncassoPropertiesInitializer {

  private final EntityManager em;

  public IncassoPropertiesInitializer(EntityManager em) {
    this.em = em;
  }

  @Transactional
  public DirectDebitConfig initializeConfig() {
    DirectDebitConfig config = new DirectDebitConfig();
    DDConfigStringEntry e = new DDConfigStringEntry();
    int year = LocalDate.now().getYear();

    e.setValue("jaarlijks-CCNL-%s");
    e.setDescription(
        "Uniek kenmerk van de machtiging; %s markeert de plek voor lidmaatschapnummer");
    config.setAuthorization(e);

    e = new DDConfigStringEntry();
    e.setValue("RCUR");
    e.setDescription("Vast waarde; herhalende incasso");
    config.setAuthorizationType(e);

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

    em.persist(config);
    return config;
  }

}
