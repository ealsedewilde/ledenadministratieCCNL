package nl.ealse.ccnl.ledenadministratie.excel.intern;

import java.io.EOFException;
import java.util.Date;
import java.util.StringJoiner;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

@Getter
public class CCNLIntern extends CCNLAdres implements Comparable<CCNLIntern> {

  private int internNummer;

  private String prefix;

  private String aanhef;

  private String functie;

  private String telefoon;

  private Date mutatiedatum;

  private String contactpersoon;

  private String naam;

  public CCNLIntern(Row row, CCNLColumnProperties properties) throws EOFException {
    super(row, properties);
    init();
  }

  private void init() throws EOFException {
    Cell cell = getCell(InternColumnDefinition.INTERN_NUMMER);
    if (cell == null || cell.getCellType() != CellType.NUMERIC) {
      throw new EOFException();
    }
    Double d = Double.valueOf(cell.getNumericCellValue());
    internNummer = d.intValue();
    cell = getCell(InternColumnDefinition.INTERN_PREFIX);
    prefix = getValue(cell);
    StringJoiner sb = new StringJoiner(" ", prefix, "");
    cell = getCell(InternColumnDefinition.INTERN_FUNCTIE);
    functie = getValue(cell);
    sb.add(functie);
    cell = getCell(InternColumnDefinition.INTERN_AANHEF);
    aanhef = getValue(cell);
    sb.add(aanhef);
    cell = getCell(InternColumnDefinition.INTERN_CONTACTPERSOON);
    contactpersoon = getValue(cell);
    sb.add(contactpersoon);
    cell = getCell(InternColumnDefinition.INTERN_TELEFOON);
    telefoon = getValue(cell);
    cell = getCell(InternColumnDefinition.INTERN_MUTATIEDATUM);
    mutatiedatum = getDateValue(cell);
    naam = sb.toString();
  }

  @Override
  public int compareTo(CCNLIntern o) {
    return this.getInternNummer() - o.getInternNummer();
  }

  @Override
  public int getRelatienummer() {
    return getInternNummer();
  }

  @Override
  public String getRelatieNaam() {
    return getFunctie();
  }

  @Override
  public int hashCode() {
    return internNummer;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CCNLIntern intern) {
      return compareTo(intern) == 0;
    }
    return false;
  }

}
