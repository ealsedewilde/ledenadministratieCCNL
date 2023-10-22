package nl.ealse.ccnl.ledenadministratie.excel;

import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:excel.properties")
public class CCNLColumnProperties {

  public static final String LEEG = "<leeg>";

  private final Environment environment;

  public CCNLColumnProperties(Environment environment) {
    this.environment = environment;
  }

  public int getKolomnummer(ColumnDefinition kolom) {
    String nummer = getProperty(kolom.name().toLowerCase());
    if (nummer == null) {
      throw new IllegalArgumentException(kolom.name());
    }
    return Integer.parseInt(nummer.trim()) - 1;
  }

  /**
   * De eerste waarde uit de reeks
   *
   * @return
   */
  public String getPropertyAutomatischeIncasso() {
    String waarde =
        getProperty(LidColumnDefinition.Property.INCASSO_AUTOMATISCH.name().toLowerCase());
    return getFirstValue(waarde);
  }
  
  public String getPropertyOverschrijving() {
    String waarde =
        getProperty(LidColumnDefinition.Property.INCASSO_OVERSCHRIJVING.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyErelid() {
    String waarde = getProperty(LidColumnDefinition.Property.INCASSO_ERELID.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyHeeftBetaald() {
    String waarde = getProperty(LidColumnDefinition.Property.HEEFT_BETAALD_JA.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyNietBetaald() {
    String waarde =
        getProperty(LidColumnDefinition.Property.HEEFT_BETAALD_NEE.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyPasVerstuurd() {
    String waarde = getProperty(LidColumnDefinition.Property.PAS_VERSTUURD_JA.name().toLowerCase());
    return getFirstValue(waarde);
  }
  
  public String getProperty(String key) {
    return environment.getProperty(key);
  }

  private String getFirstValue(String waarde) {
    String s = waarde.split(";")[0];
    if (LEEG.equals(s)) {
      return null;
    }
    return s;
  }


}
