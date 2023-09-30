package nl.ealse.ccnl.ledenadministratie.dd;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import org.springframework.stereotype.Component;

/**
 * Properties needed for generation a DirectDebit file.
 * @author ealse
 *
 */
@Component
public class IncassoProperties {

  private final EntityManager em;

  private final IncassoPropertiesInitializer initializer;

  private DirectDebitConfig config;

  public IncassoProperties(EntityManager em, IncassoPropertiesInitializer initializer) {
    this.em = em;
    this.initializer = initializer;
  }

  @PostConstruct
  public void load() {
    config = em.find(DirectDebitConfig.class, 1);
    if (config == null) {
      // delegate database update for transactional update
      // (a @PostConstruct method itself cannot be transactional.)
      config = initializer.initializeConfig();
    }
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

  public File getIncassoDirectory() {
    return new File(config.getDirectDebitDir().getValue());
  }

  /**
   * 'Test' produces a file with a maximum of 10 transactions.
   * Such a file can be used in a test run at the bank.
   * @return <code>true</code> in geval van testrun
   */
  public boolean isTest() {
    return config.getTestRun().isValue();
  }

}
