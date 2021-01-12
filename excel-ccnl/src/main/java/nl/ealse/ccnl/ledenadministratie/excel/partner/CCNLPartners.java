package nl.ealse.ccnl.ledenadministratie.excel.partner;

import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLRelaties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;

public class CCNLPartners extends CCNLRelaties<CCNLPartner> {

  public CCNLPartners(CCNLWorkbook workbook, SheetDefinition sheetName) {
    super(workbook, sheetName);
  }

}
