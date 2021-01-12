package nl.ealse.ccnl.ledenadministratie.excel.lid;

import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLRelaties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;

public class CCNLLeden extends CCNLRelaties<CCNLLid> {

  public CCNLLeden(CCNLWorkbook workbook, SheetDefinition sheetName) {
    super(workbook, sheetName);
  }

}
