package nl.ealse.ccnl.ledenadministratie.dd;

import java.math.BigDecimal;
import java.time.LocalDate;
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
  private DirectDebitConfig config = new IncassoPropertiesprovider().getProperties();

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

}
