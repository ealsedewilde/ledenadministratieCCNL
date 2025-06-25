package nl.ealse.ccnl.ledenadministratie.config;

@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

}
