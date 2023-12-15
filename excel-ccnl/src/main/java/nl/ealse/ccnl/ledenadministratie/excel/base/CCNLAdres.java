package nl.ealse.ccnl.ledenadministratie.excel.base;

import java.io.EOFException;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

@Getter
public abstract class CCNLAdres extends CCNLRow {

  private String straat;

  private String postcode;

  private String plaats;

  private String land;

  protected CCNLAdres(Row row) throws EOFException {
    super(row);
    initAddress();
  }

  private void initAddress() {
    Cell cell = getCell(AdresColumnDefinition.STRAAT_HUISNUMMER);
    straat = getValue(cell);
    cell = getCell(AdresColumnDefinition.POSTCODE);
    postcode = getValue(cell);
    cell = getCell(AdresColumnDefinition.WOONPLAATS);
    plaats = getValue(cell);
    cell = getCell(AdresColumnDefinition.LAND);
    land = getValue(cell);

  }

}
