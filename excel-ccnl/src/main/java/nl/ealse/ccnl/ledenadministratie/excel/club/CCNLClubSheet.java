package nl.ealse.ccnl.ledenadministratie.excel.club;

import java.io.EOFException;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CCNLClubSheet extends CCNLSheet<CCNLClub> {

  public CCNLClubSheet(Sheet sheet, CCNLColumnProperties properties) {
    super(sheet, properties);
  }

  @Override
  protected CCNLClub newInstance(Row currentRow) throws EOFException {
    return new CCNLClub(currentRow, getProperties());
  }

}
