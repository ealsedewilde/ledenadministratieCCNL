package nl.ealse.ccnl.ledenadministratie.excel.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Een invoer ledenbestand.
 *
 * @author Ealse
 *
 */
@Slf4j
public class CCNLWorkbook implements AutoCloseable {

  private final File bestand;

  protected Workbook workbook;

  public CCNLWorkbook(File bestand) {
    this.bestand = bestand;
    init();
  }

  public Workbook getWorkbook() {
    return workbook;
  }

  public <T extends CCNLSheet<? extends CCNLRow>> T getSheet(SheetDefinition sheet, Class<T> type) throws SheetNotFoundException {
    String sheetName = CCNLColumnProperties.getProperty(sheet.name().toLowerCase());
    Sheet excelSheet = workbook.getSheet(sheetName);
    if (excelSheet  == null) {
      throw new SheetNotFoundException(String.format("Excel tabblad '%s' niet gevonden in Excel bestand",  sheetName));
    }
    try {
      return type
          .getConstructor(Sheet.class)
          .newInstance(excelSheet);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw new CCNLRuntimeException(e);
    }
  }

  private void init() {
    String bestandsnaam = bestand.getName();
    try (InputStream is = new FileInputStream(bestand)) {
      if (bestandsnaam.toLowerCase().endsWith(".xls")) {
        workbook = new HSSFWorkbook(is);
      } else if (bestandsnaam.toLowerCase().endsWith(".xlsx")) {
        workbook = new XSSFWorkbook(is);
      }
    } catch (FileNotFoundException e) {
      log.error(String.format("Bestand %s niet gevonden", bestand.getAbsolutePath()), e);
      throw new CCNLRuntimeException(e);
    } catch (IOException e) {
      log.error(String.format("Probleem met openen bestand %s", bestand.getAbsolutePath()), e);
      throw new CCNLRuntimeException(e);
    }


  }

  @Override
  public void close() {
    try {
      workbook.close();
    } catch (IOException e) {
      log.error(String.format("Probleem met sluiten bestand %s", bestand.getAbsolutePath()), e);
    }
  }

}
