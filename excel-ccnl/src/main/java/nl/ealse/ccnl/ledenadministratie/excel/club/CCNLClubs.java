package nl.ealse.ccnl.ledenadministratie.excel.club;

import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLRelaties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;

public class CCNLClubs extends CCNLRelaties<CCNLClub> {

  public CCNLClubs(CCNLWorkbook workbook, SheetDefinition sheetName) {
    super(workbook, sheetName);
  }

}
