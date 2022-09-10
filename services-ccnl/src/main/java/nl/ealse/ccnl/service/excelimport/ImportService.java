package nl.ealse.ccnl.service.excelimport;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetNotFoundException;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler;
import nl.ealse.ccnl.ledenadministratie.excelimport.ProcessType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImportService {

  private final ImportHandler importHandler;
  private final CCNLColumnProperties properties;

  public ImportService(ImportHandler importHandler, CCNLColumnProperties properties) {
    log.info("Service created");
    this.importHandler = importHandler;
    this.properties = properties;
  }

  public void importFromExcel(File execelFile, ImportSelection selection) throws SheetNotFoundException {
    CCNLWorkbook workbook = new CCNLWorkbook(execelFile, properties);
    ProcessType importType = selection.getImportType().getProcessType();
    if (selection.isMembers()) {
      importHandler.importMembersFromExcel(workbook, importType);
    }
    if (selection.isPartners()) {
      importHandler.importPartnersFromExcel(workbook, importType);
    }
    if (selection.isClubs()) {
      importHandler.importClubsFromExcel(workbook, importType);
    }
    if (selection.isExternal()) {
      importHandler.importExternalRelationsFromExcel(workbook, importType);
    }
    if (selection.isInternal()) {
      importHandler.importInternalRelationsFromExcel(workbook, importType);
    }

    workbook.close();
  }


  @Getter
  @AllArgsConstructor
  public static class ImportSelection {

    private boolean members;
    private boolean partners;
    private boolean clubs;
    private boolean external;
    private boolean internal;

    private ImportType importType;

  }

}
