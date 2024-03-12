package nl.ealse.ccnl.test;

import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoPropertiesProvider;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig;
import nl.ealse.ccnl.ledenadministratie.model.DirectDebitConfig.DDConfigDateEntry;

public class TestIncassoPropertiesProvider implements IncassoPropertiesProvider {

  @Override
  public DirectDebitConfig getIncassoConfig() {
    DirectDebitConfig testConfig = new DirectDebitConfig();
    DDConfigDateEntry d = new DDConfigDateEntry();
    d.setValue(LocalDate.now().withMonth(4));
    d.setDescription("Datum waarop de incasso uitgevoerd moet worden");
    testConfig.setDirectDebitDate(d);
    return testConfig;
  }

}
