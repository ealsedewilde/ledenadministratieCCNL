package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.partner.PartnerColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import org.apache.poi.ss.usermodel.Row;

@UtilityClass
public class CommercialPartnerAddressExport {

  public Row addExternalRelation(CCNLBestand targetFile, ExternalRelation partner) {
    Row row = targetFile.addRow();
    targetFile.addCell(partner.getRelationNumber(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_NUMMER));
    targetFile.addCell(partner.getRelationName(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_NAAM));
    targetFile.addCell(partner.getContactNamePrefix(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_AANHEF));
    targetFile.addCell(partner.getContactName(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_CONTACTPERSOON));
    targetFile.addCell(partner.getAddress().getStreetAndNumber(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(partner.getAddress().getPostalCode(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(partner.getAddress().getCity(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(partner.getAddress().getCountry(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.LAND));
    return row;
  }

}
