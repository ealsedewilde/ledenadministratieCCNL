package nl.ealse.ccnl.ledenadministratie.excel.file;

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
    CCNLColumnProperties properties = targetFile.getProperties();
    Row row = targetFile.addRow();
    targetFile.addCell(partner.getRelationNumber(),
        properties.getKolomnummer(PartnerColumnDefinition.PARTNER_NUMMER));
    targetFile.addCell(partner.getRelationName(),
        properties.getKolomnummer(PartnerColumnDefinition.PARTNER_NAAM));
    targetFile.addCell(partner.getContactNamePrefix(),
        properties.getKolomnummer(PartnerColumnDefinition.PARTNER_AANHEF));
    targetFile.addCell(partner.getContactName(),
        properties.getKolomnummer(PartnerColumnDefinition.PARTNER_CONTACTPERSOON));
    targetFile.addCell(partner.getAddress().getAddressAndNumber(),
        properties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(partner.getAddress().getPostalCode(),
        properties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(partner.getAddress().getCity(),
        properties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(partner.getAddress().getCountry(),
        properties.getKolomnummer(AdresColumnDefinition.LAND));
    return row;
  }

}
