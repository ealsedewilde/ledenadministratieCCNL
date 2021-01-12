package nl.ealse.ccnl.ledenadministratie.excel.intern;

import java.io.EOFException;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CCNLInternSheet extends CCNLSheet<CCNLIntern> {

  public CCNLInternSheet(Sheet sheet, CCNLColumnProperties properties) {
    super(sheet, properties);
  }

  @Override
  protected CCNLIntern newInstance(Row currentRow) throws EOFException {
    return new CCNLIntern(currentRow, getProperties());
  }

}
