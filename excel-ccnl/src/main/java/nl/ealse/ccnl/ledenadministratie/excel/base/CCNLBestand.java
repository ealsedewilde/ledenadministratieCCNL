package nl.ealse.ccnl.ledenadministratie.excel.base;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class CCNLBestand implements AutoCloseable {

  private final File file;
  @Getter
  private final CCNLColumnProperties properties;

  protected Workbook workbook;

  private Sheet currentSheet;

  private Row currentRow;

  private CellStyle dateStyle;

  private CellStyle timestampStyle;

  private CellStyle currencyStyle;

  protected CCNLBestand(String bestand, CCNLColumnProperties properties) {
    this.file = new File(bestand);
    this.properties = properties;
    init();
  }

  protected CCNLBestand(File bestand, CCNLColumnProperties properties) {
    this.file = bestand;
    this.properties = properties;
    init();
  }

  public Sheet addSheet() {
    currentSheet = workbook.createSheet();
    currentRow = null;
    return currentSheet;
  }

  public Sheet addSheet(SheetDefinition sheetDef) {
    String sheetName = properties.getProperty(sheetDef.name().toLowerCase());
    currentSheet = workbook.createSheet(sheetName);
    currentRow = null;
    return currentSheet;
  }

  public Sheet addSheet(String sheetName) {
    currentSheet = workbook.createSheet(sheetName);
    currentRow = null;
    return currentSheet;
  }

  public Row addRow() {
    if (currentRow == null) {
      currentRow = currentSheet.createRow(0);
    } else {
      int rownum = currentRow.getRowNum();
      currentRow = currentSheet.createRow(++rownum);
    }
    return currentRow;
  }

  public void addCell(String waarde, int kolom) {
    Cell newCell = currentRow.createCell(kolom);
    newCell.setCellValue(waarde);
  }

  public void addCell(int waarde, int kolom) {
    Cell newCell = currentRow.createCell(kolom, CellType.NUMERIC);
    newCell.setCellValue(waarde);
  }

  public void addCell(double waarde, int kolom) {
    Cell newCell = currentRow.createCell(kolom, CellType.NUMERIC);
    newCell.setCellStyle(currencyStyle);
    newCell.setCellValue(waarde);
  }

  public void addCell(Date waarde, int kolom) {
    Cell newCell = currentRow.createCell(kolom, CellType.NUMERIC);
    newCell.setCellStyle(dateStyle);
    newCell.setCellValue(waarde);
  }

  public void addTimestampCell(Date waarde, int kolom) {
    Cell newCell = currentRow.createCell(kolom, CellType.NUMERIC);
    newCell.setCellStyle(timestampStyle);
    newCell.setCellValue(waarde);
  }

  public void addCell(Cell cell) {
    if (cell == null) {
      return;
    }
    Cell newCell = currentRow.createCell(cell.getColumnIndex(), cell.getCellType());
    switch (cell.getCellType()) {
      case BLANK:
        newCell.setCellValue("");
        break;

      case BOOLEAN:
        newCell.setCellValue(cell.getBooleanCellValue());
        break;

      case ERROR:
        newCell.setCellErrorValue(cell.getErrorCellValue());
        break;

      case FORMULA:
        newCell.setCellFormula(cell.getCellFormula());
        break;

      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          newCell.setCellStyle(dateStyle);
          newCell.setCellValue(cell.getDateCellValue());
        } else {
          newCell.setCellValue(cell.getNumericCellValue());
        }
        break;

      case STRING:
        newCell.setCellValue(cell.getStringCellValue());
        break;
      default:
        newCell.setCellFormula(cell.getCellFormula());
    }
  }

  public int addHeadingColumn(String naam, int kolom) {
    Row heading = currentSheet.getRow(0);
    Cell cell = heading.createCell(kolom);
    cell.setCellValue(naam);
    return heading.getLastCellNum() - 1;
  }

  @Override
  public void close() throws IOException {
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      Sheet sheet = workbook.getSheetAt(i);
      Row row = sheet.getRow(0);
      if (row != null) {
        for (int column = 0; column < row.getLastCellNum(); column++) {
          sheet.autoSizeColumn(column);
        }
      }
    }

    OutputStream fos = new FileOutputStream(file);
    workbook.write(fos);
    fos.close();
  }

  private void init() {
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    if (file.getName().endsWith(".xlsx")) {
      workbook = new XSSFWorkbook();
    } else {
      workbook = new HSSFWorkbook();
    }
    CreationHelper createHelper = workbook.getCreationHelper();
    dateStyle = workbook.createCellStyle();
    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
    timestampStyle = workbook.createCellStyle();
    timestampStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
    currencyStyle = workbook.createCellStyle();
    currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("€ #,##0.00"));

  }

}
