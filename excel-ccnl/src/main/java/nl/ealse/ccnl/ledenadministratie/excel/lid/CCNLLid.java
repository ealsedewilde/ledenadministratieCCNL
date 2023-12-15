package nl.ealse.ccnl.ledenadministratie.excel.lid;

import java.io.EOFException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * Een lid in het ledenbestand.
 *
 * @author Ealse
 *
 */
@Getter
public class CCNLLid extends CCNLAdres implements Comparable<CCNLLid> {
  
  private static final Date DEFAULT_DATE;
  
  static {
    Calendar c = Calendar.getInstance();
    c.set(2000, 0, 1);
    DEFAULT_DATE = c.getTime();
  }

  private int lidNummer;

  private String voornaam;

  private String tussenvoegsel;

  private String achternaam;

  private Date lidVanaf;

  private Date mutatiedatum;

  private String telefoon;

  private String ibanNummer;

  private String incassoAanduiding;

  private boolean heeftBetaald;

  private String betaalInfo;

  private boolean pasVerstuurd;

  private Date betaaldatum;

  private String email;

  private String opmerking;

  private String redenOpzegging;

  private String naam;

  public CCNLLid(Row row) throws EOFException {
    super(row);
    init();
  }

  private void init() throws EOFException {
    StringJoiner sj = new StringJoiner("");
    Cell cell = getCell(LidColumnDefinition.LIDNUMMER);
    if (cell == null || cell.getCellType() != CellType.NUMERIC) {
      throw new EOFException();
    }
    Double d = Double.valueOf(cell.getNumericCellValue());
    lidNummer = d.intValue();
    cell = getCell(LidColumnDefinition.VOORLETTERS);
    voornaam = getValue(cell);
    if (voornaam != null) {
      sj.add(voornaam);
    }
    cell = getCell(LidColumnDefinition.TUSSENVOEGSEL);
    tussenvoegsel = getValue(cell);
    if (tussenvoegsel != null && tussenvoegsel.length() > 0) {
      sj.add(tussenvoegsel);
    }
    cell = getCell(LidColumnDefinition.ACHTERNAAM);
    achternaam = getValue(cell);
    sj.add(achternaam);
    cell = getCell(LidColumnDefinition.TELEFOON);
    telefoon = getValue(cell);
    cell = getCell(LidColumnDefinition.IBAN_NUMMER);
    ibanNummer = getValue(cell);
    cell = getCell(LidColumnDefinition.INCASSO);
    incassoAanduiding = getValue(cell);
    cell = getCell(LidColumnDefinition.HEEFT_BETAALD);
    heeftBetaald = checkBoolean(getValue(cell), LidColumnDefinition.Property.HEEFT_BETAALD_JA);
    cell = getCell(LidColumnDefinition.BETAAL_INFO);
    betaalInfo = getValue(cell);
    cell = getCell(LidColumnDefinition.PAS_VERSTUURD);
    pasVerstuurd = checkBoolean(getValue(cell), LidColumnDefinition.Property.PAS_VERSTUURD_JA);
    cell = getCell(LidColumnDefinition.BETAALDATUM);
    betaaldatum = getDateValue(cell);
    cell = getCell(LidColumnDefinition.MUTATIEDATUM);
    mutatiedatum = getDateValue(cell);
    cell = getCell(LidColumnDefinition.LID_VANAF);
    lidVanaf = getDateValue(cell);
    if (lidVanaf == null) {
      lidVanaf = DEFAULT_DATE;
    }
    cell = getCell(LidColumnDefinition.EMAIL);
    email = getValue(cell);
    cell = getCell(LidColumnDefinition.OPMERKING);
    opmerking = getValue(cell);
    cell = getCell(LidColumnDefinition.REDEN_OPZEGGING);
    redenOpzegging = getValue(cell);
    naam = sj.toString();

  }


  private boolean checkBoolean(String cellWaarde, LidColumnDefinition.Property trueValueProperty) {
    String waarde = CCNLColumnProperties.getProperty(trueValueProperty.name().toLowerCase());
    String[] waarden = waarde.split(";");
    for (String w : waarden) {
      if (w.equalsIgnoreCase(cellWaarde)) {
        return true;
      }
    }
    return false;
  }

  public boolean buitenlandsAdres() {
    return getLand() != null;
  }

  public String toString() {
    StringJoiner builder = new StringJoiner(", ");
    builder.add(Integer.toString(lidNummer));
    builder.add(achternaam);
    builder.add(ibanNummer);
    return builder.toString();
  }

  public boolean isAutomatischeIncasso() {
    return checkBoolean(incassoAanduiding, LidColumnDefinition.Property.INCASSO_AUTOMATISCH);
  }

  public boolean isOverschrijving() {
    return checkBoolean(incassoAanduiding, LidColumnDefinition.Property.INCASSO_OVERSCHRIJVING);
  }

  public boolean isErelid() {
    return checkBoolean(incassoAanduiding, LidColumnDefinition.Property.INCASSO_ERELID);
  }

  public boolean isEenmalig() {
    return checkBoolean(incassoAanduiding, LidColumnDefinition.Property.INCASSO_EENMALIG);
  }

  @Override
  public int compareTo(CCNLLid ander) {
    return this.getLidNummer() - ander.getLidNummer();
  }

  @Override
  public int getRelatienummer() {
    return getLidNummer();
  }

  @Override
  public String getRelatieNaam() {
    return getAchternaam();
  }

  public boolean equals(Object o) {
    if (o instanceof CCNLLid lid) {
      return lidNummer == lid.lidNummer;
    }
    return false;
  }

  public int hashCode() {
    return lidNummer;
  }

}
