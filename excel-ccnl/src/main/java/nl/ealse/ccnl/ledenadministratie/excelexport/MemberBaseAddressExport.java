package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.MemberBase;

@UtilityClass
public class MemberBaseAddressExport {

  void addMember(CCNLBestand targetFile, MemberBase member) {
    CCNLColumnProperties properties = targetFile.getProperties();
    targetFile.addRow();
    targetFile.addCell(member.getInitials(),
        properties.getKolomnummer(LidColumnDefinition.VOORLETTERS));
    targetFile.addCell(member.getLastNamePrefix(),
        properties.getKolomnummer(LidColumnDefinition.TUSSENVOEGSEL));
    targetFile.addCell(member.getLastName(),
        properties.getKolomnummer(LidColumnDefinition.ACHTERNAAM));
    targetFile.addCell(member.getAddress().getAddressAndNumber(),
        properties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(member.getAddress().getPostalCode(),
        properties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(member.getAddress().getCity(),
        properties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(member.getAddress().getCountry(),
        properties.getKolomnummer(AdresColumnDefinition.LAND));
  }

}
