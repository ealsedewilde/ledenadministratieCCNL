package nl.ealse.ccnl.ledenadministratie.dd;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ServiceLoader;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;

/**
 * Properties needed for generation a DirectDebit file.
 * 
 * @author ealse
 *
 */
@UtilityClass
public class IncassoProperties {
  
  @Getter
  private final DirectDebitConfig properties;
  
  static {
    ServiceLoader<IncassoPropertiesProvider> serviceLoader =
        ServiceLoader.load(IncassoPropertiesProvider.class);
    Optional<IncassoPropertiesProvider> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      properties = first.get().getIncassoConfig();
    } else {
      properties = new DefaultIncassoPropertiesProvider().getIncassoConfig();
    }
  }

  public String getIbanNummer() {
    return properties.getIbanNumber().getValue();
  }

  public String getIncassantId() {
    return properties.getDirectDebitId().getValue();
  }

  public BigDecimal getIncassoBedrag() {
    return properties.getDirectDebitAmount().getValue();
  }

  public LocalDate getIncassoDatum() {
    return properties.getDirectDebitDate().getValue();
  }

  public String getIncassoReden() {
    return properties.getDirectDebitDescription().getValue();
  }

  public String getMachtigingReferentie() {
    return properties.getAuthorization().getValue();
  }

  public String getMachtigingType() {
    return properties.getAuthorizationType().getValue();
  }

  public String getMessageId() {
    return properties.getMessageId().getValue();
  }

  public String getNaam() {
    return properties.getClubName().getValue();
  }

  public String getIncassoDirectory() {
    return properties.getDirectDebitDir().getValue();
  }

  /**
   * 'Test' produces a file with a maximum of 10 transactions. Such a file can be used in a test run
   * at the bank.
   * 
   * @return <code>true</code> in geval van testrun
   */
  public boolean isTest() {
    return properties.getTestRun().isValue();
  }

}
