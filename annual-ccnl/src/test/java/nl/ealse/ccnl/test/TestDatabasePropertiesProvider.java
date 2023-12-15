package nl.ealse.ccnl.test;

import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.config.DatabasePropertiesProvider;

public class TestDatabasePropertiesProvider implements DatabasePropertiesProvider{

  @Override
  public Properties getProperties() {
    Properties props = new Properties();
    props.put("ccnl.contributie.incasso", "27,50");
    props.put("ccnl.contributie.overboeken", "30,00");
    return props;
  }

}
