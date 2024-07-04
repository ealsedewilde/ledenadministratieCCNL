package nl.ealse.ccnl.service.excelimport;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetNotFoundException;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler.ImportSelection;

@Slf4j
public class ImportService {

  private final ImportHandler importHandler;

  public ImportService(ImportHandler importHandler) {
    log.info("Service created");
    this.importHandler = importHandler;
  }

  public void importFromExcel(File execelFile, ImportSelection selection)
      throws SheetNotFoundException {
    CCNLWorkbook workbook = new CCNLWorkbook(execelFile);
    importHandler.importFromExcel(workbook, selection);
    workbook.close();
  }


}
