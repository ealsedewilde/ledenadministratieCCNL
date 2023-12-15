package nl.ealse.ccnl.ledenadministratie.excel.dd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Opvragen BIC-codes voor Nederlandse banken.
 *
 * @author Ealse
 *
 */
@UtilityClass
@Slf4j
public class BicResolver {

  private static final String FILE_NAME = "BIC-lijst-NL.xlsx";
  private static final File BIC_LIST_FILE = new File(FILE_NAME);

  private Map<String, String> bicMap = new HashMap<>();

  static {
    init();
  }

  /**
   * BIC-code bij IBAN-nummer opvragen.
   * @param ibanNummer
   * @return BIC-code
   */
  public String getBicCode(String ibanNummer) {
    String bankcode = ibanNummer.substring(4, 8);
    String biccode = bicMap.get(bankcode);
    if (biccode == null) {
      throw new IllegalArgumentException("Onbekende bank bij IBAN: " + ibanNummer);
    }
    return biccode;
  }

  private void init() {
    Workbook workbook = null;
    try (InputStream is = getBicListInputStream()) {
      workbook = new XSSFWorkbook(is);
      Sheet sheet = workbook.getSheetAt(0);

      Iterator<Row> itr = sheet.rowIterator();
      for (Row row = itr.next(); itr.hasNext(); row = itr.next()) {
        Cell bankCell = row.getCell(0);
        if ("BIC".equals(bankCell.getStringCellValue())) {
          break;
        }
      }
      
      for (Row row = itr.next(); itr.hasNext(); row = itr.next()) {
        Cell bankCell = row.getCell(1);
        Cell bicCodeCell = row.getCell(0);
        bicMap.put(bankCell.getStringCellValue(), bicCodeCell.getStringCellValue());
      }
    } catch (IOException e) {
      log.error("Fout bij inlezen bestand '" + FILE_NAME + "'", e);
      throw new ExceptionInInitializerError("Fout bij inlezen bestand '" + FILE_NAME + "'");
    } finally {
      closeWorkbook(workbook);
    }
  }

  private InputStream getBicListInputStream() throws IOException {
    if (BIC_LIST_FILE.exists()) {
      log.info("Gebruikt BIC-code bestand: " + BIC_LIST_FILE.getAbsolutePath());
      return new FileInputStream(BIC_LIST_FILE);
    }
    log.info("Gebruikt intern BIC-code bestand"); 
    return BicResolver.class.getResourceAsStream("/" + FILE_NAME);
  }

  private void closeWorkbook(Workbook workbook) {
    if (workbook != null) {
      try {
        workbook.close();
      } catch (IOException e) {
        log.error("Error closing Excel '" + FILE_NAME + "'", e);
      }
    }

  }

}
