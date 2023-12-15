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
    targetFile.addRow();
    targetFile.addCell(member.getInitials(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.VOORLETTERS));
    targetFile.addCell(member.getLastNamePrefix(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.TUSSENVOEGSEL));
    targetFile.addCell(member.getLastName(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.ACHTERNAAM));
    targetFile.addCell(member.getAddress().getStreetAndNumber(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(member.getAddress().getPostalCode(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(member.getAddress().getCity(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(member.getAddress().getCountry(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.LAND));
  }

}
