package nl.ealse.ccnl.ledenadministratie.excel.dd;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Opvragen BIC-codes voor Nederlandse banken
 * 
 * @author Ealse
 *
 */
@UtilityClass
@Slf4j
public class BicResolver {

  private static final String FILE_NAME = "BIC-lijst-NL.xlsx";

  private Map<String, String> bicMap = new HashMap<>();

  static {
    init();
  }

  /**
   * 
   * @param ibanNummer
   * @return
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
    Resource bicLijst = new ClassPathResource(FILE_NAME);
    Workbook workbook = null;
    try (InputStream is = bicLijst.getInputStream()) {
      workbook = new XSSFWorkbook(is);
      Sheet sheet = workbook.getSheetAt(0);

      Iterator<Row> itr = sheet.rowIterator();
      itr.next();
      itr.next();
      for (Row row = itr.next(); itr.hasNext(); row = itr.next()) {
        Cell bankCell = row.getCell(0);
        Cell bicCodeCell = row.getCell(1);
        bicMap.put(bankCell.getStringCellValue(), bicCodeCell.getStringCellValue());
      }
    } catch (IOException e) {
      log.error("Fout bij inlezen bestand '" + FILE_NAME + "'", e);
      throw new ExceptionInInitializerError("Fout bij inlezen bestand '" + FILE_NAME + "'");
    } finally {
      closeWorkbook(workbook);
    }
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
