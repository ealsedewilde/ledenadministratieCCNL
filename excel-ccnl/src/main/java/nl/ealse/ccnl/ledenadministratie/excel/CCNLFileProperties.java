package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.File;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Ealse
 *
 */
public abstract class CCNLFileProperties {

  private final Environment environment;

  protected CCNLFileProperties(Environment environment) {
    this.environment = environment;
  }

  public String getProperty(String key) {
    return environment.getProperty(key);
  }

  public File basis() {
    return new File(environment.getProperty("basis"));
  }

  public File ledenbestandInFile() {
    return new File(basis(), environment.getProperty("input.ledenbestand"));
  }

  public String ledenbestandIn() {
    return environment.getProperty("input.ledenbestand");
  }

  public File ledenbestandOutFile() {
    return new File(basis(), environment.getProperty("output.ledenbestand"));
  }

  public String ledenbestandOut() {
    return environment.getProperty("output.ledenbestand");
  }

  public File adresbestandOutFile() {
    return new File(basis(), environment.getProperty("output.adresbestand"));
  }

  public String adresbestandOut() {
    return environment.getProperty("output.adresbestand");
  }

  public File incassobestandOutFile() {
    return new File(basis(), environment.getProperty("output.incassobestand"));
  }

  public String incassobestandOut() {
    return environment.getProperty("output.incassobestand");
  }
}
