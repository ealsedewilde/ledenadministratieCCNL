package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.intern.InternColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import org.apache.poi.ss.usermodel.Row;

@UtilityClass
public class InternalRelationAddressExport {

  public Row addInternalRelation(CCNLBestand targetFile, InternalRelation intern) {
    Row row = targetFile.addRow();
    targetFile.addCell(intern.getRelationNumber(),
        CCNLColumnProperties.getKolomnummer(InternColumnDefinition.INTERN_NUMMER));
    targetFile.addCell(CCNLColumnProperties.getProperty("blad_prefix") + intern.getTitle(),
        CCNLColumnProperties.getKolomnummer(InternColumnDefinition.INTERN_PREFIX));
    targetFile.addCell(CCNLColumnProperties.getProperty("blad_aanhef"),
        CCNLColumnProperties.getKolomnummer(InternColumnDefinition.INTERN_AANHEF));
    targetFile.addCell(intern.getContactName(),
        CCNLColumnProperties.getKolomnummer(InternColumnDefinition.INTERN_FUNCTIE));
    targetFile.addCell(intern.getAddress().getStreetAndNumber(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(intern.getAddress().getPostalCode(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(intern.getAddress().getCity(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(intern.getAddress().getCountry(),
        CCNLColumnProperties.getKolomnummer(AdresColumnDefinition.LAND));
    return row;
  }

}
