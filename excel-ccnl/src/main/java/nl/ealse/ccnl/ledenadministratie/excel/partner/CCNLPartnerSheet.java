package nl.ealse.ccnl.ledenadministratie.excel.partner;

import java.io.EOFException;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CCNLPartnerSheet extends CCNLSheet<CCNLPartner> {

  public CCNLPartnerSheet(Sheet sheet) {
    super(sheet);
  }

  @Override
  protected CCNLPartner newInstance(Row currentRow) throws EOFException {
    return new CCNLPartner(currentRow);
  }

}
