package nl.ealse.ccnl.ledenadministratie.excel.base;

import java.io.EOFException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Een werkblad in het ledenbestand
 *
 * @author Ealse
 *
 */
public abstract class CCNLSheet<T extends CCNLRow> implements Iterable<T> {

  private final Sheet sheet;
  @Getter
  private final CCNLColumnProperties properties;

  private Row headerRow;

  private Row currentRow;

  private int ix = 0;

  protected CCNLSheet(Sheet sheet, CCNLColumnProperties properties) {
    this.sheet = sheet;
    this.properties = properties;
    if (sheet != null) {
      init();
    }
  }

  private void init() {
    Row firstRow = sheet.getRow(0);
    if (firstRow != null) {
      Cell cell = firstRow.getCell(0);
      if (cell != null && cell.getCellType() != CellType.NUMERIC) {
        headerRow = sheet.getRow(0);
        ix = 1;
      }
    }
  }

  public String getName() {
    if (sheet != null) {
      return sheet.getSheetName();
    }
    return null;
  }

  public Sheet getSheet() {
    return sheet;
  }

  public Row getHeaderRow() {
    return headerRow;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {

      @Override
      public boolean hasNext() {
        if (sheet == null) {
          return false;
        }
        currentRow = sheet.getRow(ix);
        if (currentRow == null) {
          return false;
        }
        try {
          newInstance(currentRow);
          return true;
        } catch (EOFException e) {
          return false;
        }
      }

      @Override
      public T next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        currentRow = sheet.getRow(ix++);
        try {
          return newInstance(currentRow);
        } catch (Exception e) {
          throw new IllegalStateException();
        }
      }

      @Override
      public void remove() {
        if (currentRow == null) {
          throw new IllegalStateException();
        }
        sheet.removeRow(currentRow);

      }

    };
  }

  protected abstract T newInstance(Row currentRow) throws EOFException;

}
