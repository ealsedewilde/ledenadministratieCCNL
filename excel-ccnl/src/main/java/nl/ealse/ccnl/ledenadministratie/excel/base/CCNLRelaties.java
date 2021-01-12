package nl.ealse.ccnl.ledenadministratie.excel.base;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Row;

public class CCNLRelaties<T extends CCNLRow> implements Iterable<T> {
  private SortedMap<Integer, T> relaties = new TreeMap<>();

  private Row heading;

  public Row getHeading() {
    return heading;
  }

  public CCNLRelaties(CCNLWorkbook workbook, SheetDefinition sheetName) {
    init(workbook, sheetName);
  }

  private void init(CCNLWorkbook workbook, SheetDefinition sheetName) {
    @SuppressWarnings("unchecked")
    CCNLSheet<T> sheet = (CCNLSheet<T>) workbook.getSheet(sheetName);
    heading = sheet.getSheet().getRow(0);
    for (T relatie : sheet) {
      relaties.put(relatie.getRelatienummer(), relatie);
    }
  }

  public Collection<T> getRelaties() {
    return relaties.values();
  }

  @Override
  public Iterator<T> iterator() {
    return relaties.values().iterator();
  }

  public T getRelatie(int relatienummer) {
    return relaties.get(relatienummer);
  }


  public CCNLRow zoekRelatieOpNaam(String achternaam) {
    for (CCNLRow relatie : relaties.values()) {
      if (achternaam.equals(relatie.getRelatieNaam())) {
        return relatie;
      }
    }
    return null;
  }



}
