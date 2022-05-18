package nl.ealse.ccnl.ledenadministratie.excel.partner;

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
public class CCNLPartner extends CCNLAdres implements Comparable<CCNLPartner> {

  private int partnerNummer;

  private String partnerNaam;

  private String aanhef;

  private String contactpersoon;

  private Date partnerVanaf;

  private Date mutatiedatum;

  private String telefoon;

  private String email;

  private String opmerking;

  private String naam;

  public CCNLPartner(Row row, CCNLColumnProperties properties) throws EOFException {
    super(row, properties);
    initPartner();

  }

  private void initPartner() throws EOFException {
    StringJoiner sb = new StringJoiner(" ");
    Cell cell = getCell(PartnerColumnDefinition.PARTNER_NUMMER);
    if (cell == null || cell.getCellType() != CellType.NUMERIC) {
      throw new EOFException();
    }
    Double d = Double.valueOf(cell.getNumericCellValue());
    partnerNummer = d.intValue();
    cell = getCell(PartnerColumnDefinition.PARTNER_NAAM);
    partnerNaam = getValue(cell);
    sb.add(partnerNaam);
    cell = getCell(PartnerColumnDefinition.PARTNER_AANHEF);
    aanhef = getValue(cell);
    if (aanhef != null) {
      sb.add(aanhef);
    }
    cell = getCell(PartnerColumnDefinition.PARTNER_CONTACTPERSOON);
    contactpersoon = getValue(cell);
    if (contactpersoon != null && contactpersoon.length() > 0) {
      sb.add(contactpersoon);
    }
    cell = getCell(PartnerColumnDefinition.PARTNER_TELEFOON);
    telefoon = getValue(cell);
    cell = getCell(PartnerColumnDefinition.PARTNER_MUTATIEDATUM);
    mutatiedatum = getDateValue(cell);
    cell = getCell(PartnerColumnDefinition.PARTNER_VANAF);
    partnerVanaf = getDateValue(cell);
    cell = getCell(PartnerColumnDefinition.PARTNER_EMAIL);
    email = getValue(cell);
    cell = getCell(PartnerColumnDefinition.PARTNER_OPMERKING);
    opmerking = getValue(cell);

    naam = sb.toString();
  }

  @Override
  public int compareTo(CCNLPartner ander) {
    return this.partnerNummer - ander.partnerNummer;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CCNLPartner partner) {
      return compareTo(partner) == 0;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return partnerNummer;
  }

  @Override
  public int getRelatienummer() {
    return getPartnerNummer();
  }

  @Override
  public String getRelatieNaam() {
    return getPartnerNaam();
  }

}
