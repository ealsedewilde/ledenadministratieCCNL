package nl.ealse.ccnl.ledenadministratie.excel.club;

import java.io.EOFException;
import java.util.StringJoiner;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

@Getter
public class CCNLClub extends CCNLAdres implements Comparable<CCNLClub> {

  private int clubNummer;

  private String clubNaam;

  private String aanhef;

  private String contactpersoon;

  private String naam;

  public CCNLClub(Row row) throws EOFException {
    super(row);
    initClub();
  }

  private void initClub() throws EOFException {
    StringJoiner sb = new StringJoiner(" ");
    Cell cell = getCell(ClubColumnDefinition.CLUB_NUMMER);
    if (cell == null || cell.getCellType() != CellType.NUMERIC) {
      throw new EOFException();
    }
    Double d = Double.valueOf(cell.getNumericCellValue());
    clubNummer = d.intValue();
    cell = getCell(ClubColumnDefinition.CLUB_NAAM);
    clubNaam = getValue(cell);
    sb.add(clubNaam);
    cell = getCell(ClubColumnDefinition.CLUB_AANHEF);
    aanhef = getValue(cell);
    sb.add(aanhef);
    cell = getCell(ClubColumnDefinition.CLUB_CONTACTPERSOON);
    contactpersoon = getValue(cell);
    sb.add(contactpersoon);
    naam = sb.toString();
  }

  @Override
  public int compareTo(CCNLClub o) {
    return this.getClubNummer() - o.getClubNummer();
  }

  @Override
  public int getRelatienummer() {
    return getClubNummer();
  }

  @Override
  public String getRelatieNaam() {
    return getClubNaam();
  }

  @Override
  public int hashCode() {
    return clubNummer;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CCNLClub club) {
      return compareTo(club) == 0;
    }
    return false;
  }

}
