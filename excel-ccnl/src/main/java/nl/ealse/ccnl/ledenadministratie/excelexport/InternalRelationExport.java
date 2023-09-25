package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.intern.InternColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;
import org.apache.poi.ss.usermodel.Row;

@UtilityClass
public class InternalRelationExport {

  public Row addInternalRelation(CCNLBestand targetFile, InternalRelation intern) {
    CCNLColumnProperties properties = targetFile.getProperties();
    Row row = targetFile.addRow();
    targetFile.addCell(intern.getRelationNumber(),
        properties.getKolomnummer(InternColumnDefinition.INTERN_NUMMER));
    targetFile.addCell(properties.getProperty("blad_prefix"),
        properties.getKolomnummer(InternColumnDefinition.INTERN_PREFIX));
    targetFile.addCell(properties.getProperty("blad_aanhef"),
        properties.getKolomnummer(InternColumnDefinition.INTERN_AANHEF));
    targetFile.addCell(intern.getTitle(),
        properties.getKolomnummer(InternColumnDefinition.INTERN_FUNCTIE));
    targetFile.addCell(intern.getContactName(),
        properties.getKolomnummer(InternColumnDefinition.INTERN_CONTACTPERSOON));
    targetFile.addCell(intern.getAddress().getStreetAndNumber(),
        properties.getKolomnummer(AdresColumnDefinition.STRAAT_HUISNUMMER));
    targetFile.addCell(intern.getAddress().getPostalCode(),
        properties.getKolomnummer(AdresColumnDefinition.POSTCODE));
    targetFile.addCell(intern.getAddress().getCity(),
        properties.getKolomnummer(AdresColumnDefinition.WOONPLAATS));
    targetFile.addCell(intern.getAddress().getCountry(),
        properties.getKolomnummer(AdresColumnDefinition.LAND));
    targetFile.addCell(intern.getTelephoneNumber(),
        properties.getKolomnummer(InternColumnDefinition.INTERN_TELEFOON));
    targetFile.addCell(DateUtil.toDate(intern.getModificationDate()),
        properties.getKolomnummer(InternColumnDefinition.INTERN_MUTATIEDATUM));
    return row;
  }

}
