package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.MagazineColumnDefinition;

@Slf4j
@UtilityClass
public class CCNLColumnProperties {

  private static final String PROPERTY_FILE = "/excel.properties";
  public static final String LEEG = "<leeg>";
  private static final Properties properties = new Properties();

  static {
    try (InputStream is = CCNLColumnProperties.class.getResourceAsStream(PROPERTY_FILE)) {
      properties.load(is);
    } catch (IOException e) {
      String msg = "Failed to load excel.properties";
      log.error(msg, e);
    }
  }

  public int getKolomnummer(ColumnDefinition kolom) {
    String nummer = properties.getProperty(kolom.name().toLowerCase());
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
    String waarde = properties
        .getProperty(LidColumnDefinition.Property.INCASSO_AUTOMATISCH.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyOverschrijving() {
    String waarde = properties
        .getProperty(LidColumnDefinition.Property.INCASSO_OVERSCHRIJVING.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyErelid() {
    String waarde =
        properties.getProperty(LidColumnDefinition.Property.INCASSO_ERELID.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyHeeftBetaald() {
    String waarde =
        properties.getProperty(LidColumnDefinition.Property.HEEFT_BETAALD_JA.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyPasMeesturen() {
    String waarde =
        properties.getProperty(MagazineColumnDefinition.Property.PAS_MEESTUREN_JA.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyNietBetaald() {
    String waarde =
        properties.getProperty(LidColumnDefinition.Property.HEEFT_BETAALD_NEE.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyPasVerstuurd() {
    String waarde =
        properties.getProperty(LidColumnDefinition.Property.PAS_VERSTUURD_JA.name().toLowerCase());
    return getFirstValue(waarde);
  }
  
  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  private String getFirstValue(String waarde) {
    String s = waarde.split(";")[0];
    if (LEEG.equals(s)) {
      return null;
    }
    return s;
  }


}
