package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.club.ClubColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import org.apache.poi.ss.usermodel.Row;

@UtilityClass
public class ExternalClubExport {

  public Row addExternalRelation(CCNLBestand targetFile, ExternalRelationClub club) {
    Row row = targetFile.addRow();
    targetFile.addCell(club.getRelationNumber(),
        CCNLColumnProperties.getKolomnummer(ClubColumnDefinition.CLUB_NUMMER));
    targetFile.addCell(club.getRelationName(),
        CCNLColumnProperties.getKolomnummer(ClubColumnDefinition.CLUB_NAAM));
    targetFile.addCell(club.getContactNamePrefix(),
        CCNLColumnProperties.getKolomnummer(ClubColumnDefinition.CLUB_AANHEF));
    targetFile.addCell(club.getContactName(),
        CCNLColumnProperties.getKolomnummer(ClubColumnDefinition.CLUB_CONTACTPERSOON));
    targetFile.addCell(club.getAddress().getStreetAndNumber(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(club.getAddress().getPostalCode(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(club.getAddress().getCity(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(club.getAddress().getCountry(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.LAND));
    return row;
  }


}
