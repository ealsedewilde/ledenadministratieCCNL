package nl.ealse.ccnl.test;

import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.config.DatabasePropertiesProvider;

public class TestDatabasePropertiesProvider implements DatabasePropertiesProvider{

  @Override
  public Properties getProperties() {
    Properties props = new Properties();
    props.put("ccnl.mail.host", "localhost");
    props.put("ccnl.mail.port", "25");
    props.put("ccnl.mail.properties.mail.smtp.auth", "false");
    props.put("ccnl.mail.properties.mail.smtp.starttls.enable", "false");
    props.put("ccnl.mail.subject", "test mail");
    return props;
  }

}
