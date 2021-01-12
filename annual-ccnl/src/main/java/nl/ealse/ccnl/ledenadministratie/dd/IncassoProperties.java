package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import org.springframework.stereotype.Component;

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
  public void postConstruct() {
    config = em.find(DirectDebitConfig.class, 1);
    if (config == null) {
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

  public boolean isTest() {
    return config.getTestRun().isValue();
  }

}
