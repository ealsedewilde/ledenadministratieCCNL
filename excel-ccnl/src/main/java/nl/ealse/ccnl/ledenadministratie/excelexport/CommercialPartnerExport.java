package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.partner.PartnerColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;
import org.apache.poi.ss.usermodel.Row;

@UtilityClass
public class CommercialPartnerExport {

  public Row addExternalRelation(CCNLBestand targetFile, ExternalRelation partner) {
    Row row = CommercialPartnerAddressExport.addExternalRelation(targetFile, partner);
    targetFile.addCell(partner.getTelephoneNumber(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_TELEFOON));
    targetFile.addCell(DateUtil.toDate(partner.getRelationSince()),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_VANAF));
    targetFile.addCell(DateUtil.toDate(partner.getModificationDate()),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_MUTATIEDATUM));
    targetFile.addCell(partner.getEmail(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_EMAIL));
    targetFile.addCell(partner.getRelationInfo(),
        CCNLColumnProperties.getKolomnummer(PartnerColumnDefinition.PARTNER_OPMERKING));
    return row;
  }

}
