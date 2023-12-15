package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excelexport.ArchivedMemberExport;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;

public class Archiefbestand extends CCNLBestand {

  public Archiefbestand(File bestand) throws IOException {
    super(bestand);
  }

  public void addHeading() {
    addRow();
    List<ColumnDefinition> list = new ArrayList<>();
    list.addAll(Arrays.asList(LidColumnDefinition.values()));
    list.addAll(Arrays.asList(AdresColumnDefinition.values()));
    list.forEach(c -> addCell(c.heading(), CCNLColumnProperties.getKolomnummer(c)));
  }

  public void addMember(ArchivedMember member) {
    ArchivedMemberExport.addMember(this, member);
  }

}
