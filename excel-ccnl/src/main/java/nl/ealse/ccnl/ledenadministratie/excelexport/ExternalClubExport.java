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
    CCNLColumnProperties properties = targetFile.getProperties();
    Row row = targetFile.addRow();
    targetFile.addCell(club.getRelationNumber(),
        properties.getKolomnummer(ClubColumnDefinition.CLUB_NUMMER));
    targetFile.addCell(club.getRelationName(),
        properties.getKolomnummer(ClubColumnDefinition.CLUB_NAAM));
    targetFile.addCell(club.getContactNamePrefix(),
        properties.getKolomnummer(ClubColumnDefinition.CLUB_AANHEF));
    targetFile.addCell(club.getContactName(),
        properties.getKolomnummer(ClubColumnDefinition.CLUB_CONTACTPERSOON));
    targetFile.addCell(club.getAddress().getAddressAndNumber(),
        properties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(club.getAddress().getPostalCode(),
        properties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(club.getAddress().getCity(),
        properties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(club.getAddress().getCountry(),
        properties.getKolomnummer(AdresColumnDefinition.LAND));
    return row;
  }


}
