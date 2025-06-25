package nl.ealse.ccnl.ledenadministratie.excel;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.MagazineColumnDefinition;

@UtilityClass
public class CCNLColumnProperties {

  public static final String LEEG = "<leeg>";

  public int getKolomnummer(ColumnDefinition kolom) {
    String nummer = ApplicationContext.getProperty(kolom.name().toLowerCase());
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
    String waarde = ApplicationContext
        .getProperty(LidColumnDefinition.Property.INCASSO_AUTOMATISCH.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyOverschrijving() {
    String waarde = ApplicationContext
        .getProperty(LidColumnDefinition.Property.INCASSO_OVERSCHRIJVING.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyErelid() {
    String waarde =
        ApplicationContext.getProperty(LidColumnDefinition.Property.INCASSO_ERELID.name().toLowerCase());
    return getFirstValue(waarde);
  }

  public String getPropertyHeeftBetaald() {
    String waarde =
        ApplicationContext.getProperty(LidColumnDefinition.Property.HEEFT_BETAALD_JA.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyPasMeesturen() {
    String waarde =
        ApplicationContext.getProperty(MagazineColumnDefinition.Property.PAS_MEESTUREN_JA.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyNietBetaald() {
    String waarde =
        ApplicationContext.getProperty(LidColumnDefinition.Property.HEEFT_BETAALD_NEE.name().toLowerCase());
    return getFirstValue(waarde);

  }

  public String getPropertyPasVerstuurd() {
    String waarde =
        ApplicationContext.getProperty(LidColumnDefinition.Property.PAS_VERSTUURD_JA.name().toLowerCase());
    return getFirstValue(waarde);
  }
  
  public String getProperty(String key) {
    return ApplicationContext.getProperty(key);
  }

  private String getFirstValue(String waarde) {
    String s = waarde.split(";")[0];
    if (LEEG.equals(s)) {
      return null;
    }
    return s;
  }


}
