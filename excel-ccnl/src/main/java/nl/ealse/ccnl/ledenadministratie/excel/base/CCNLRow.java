package nl.ealse.ccnl.ledenadministratie.excel.base;

import java.io.EOFException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Ealse
 *
 */
@Slf4j
@Getter
public abstract class CCNLRow {

  private final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

  private final Row row;

  private final CellStyle dateStyle;

  protected CCNLRow(Row row) throws EOFException {
    this.row = row;
    this.dateStyle = initDateStyle();
  }

  private CellStyle initDateStyle() throws EOFException {
    Workbook workbook = getRow().getSheet().getWorkbook();
    CreationHelper createHelper = workbook.getCreationHelper();
    CellStyle ds = workbook.createCellStyle();
    ds.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
    return ds;
  }

  public Cell getCell(ColumnDefinition kolom) {
    return row.getCell(CCNLColumnProperties.getKolomnummer(kolom));
  }

  protected String getValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case BOOLEAN:
        return Boolean.toString(cell.getBooleanCellValue());
      case NUMERIC:
        return Double.toString(cell.getNumericCellValue());
      case BLANK:
        return null;
      default:
        log.error("fout cell type: " + cell.getCellType());
    }
    return cell.getStringCellValue().trim();
  }

  protected Date getDateValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    if (CellType.NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
      return cell.getDateCellValue();
    } else if (CellType.STRING == cell.getCellType()) {
      return toDate(cell.getStringCellValue().trim());
    }
    return null;
  }

  /*
   * The dates in original LedenadministratieExcel are inconsistent. 
   * We try to convert as much as possible.
   */
  private Date toDate(String s) {
    if (s.length() == 0) {
      return null;
    }
    try {
      if (s.length() == 2) {
        // just a year; no day or month
        int yy = Integer.parseInt(s);
        if (yy > 50) {
          return df.parse("01-01-19" + s);
        } else {
          return df.parse("01-01-20" + s);
        }
      } else {
        return df.parse(s);
      }
    } catch (NumberFormatException | ParseException e) {
      log.warn("Invalid date: "+s);
      return null;
    }

  }

  public abstract int getRelatienummer();

  public abstract String getRelatieNaam();

}
