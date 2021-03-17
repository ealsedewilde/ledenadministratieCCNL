package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.File;
import java.io.IOException;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ArchiveId;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.MemberBase;
import nl.ealse.ccnl.test.util.ExcelPropertiesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArchiefbestandTest {
  
  @TempDir
  File tempDir;
  
  private Archiefbestand sut;
  
  private CCNLColumnProperties ccnlColumnProperties;
  
  @Test
  void testArchiefbestand() {
    ccnlColumnProperties = ExcelPropertiesFactory.newExcelProperties();
    File f = new File(tempDir, "archiefTest.xlsx");
    try {
      sut = new Archiefbestand(f, ccnlColumnProperties);
      sut.addSheet("test");
      sut.addHeading();
      sut.addMember(member());
      sut.close();
      long l = f.length();
      
      boolean ok = l > 3875 || l < 3880;
      Assertions.assertTrue(ok);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  private ArchivedMember member() {
    ArchivedMember am = new ArchivedMember();
    ArchiveId id = new ArchiveId();
    am.setId(id);
    MemberBase m = new MemberBase();
    am.setMember(m);
    id.setArchiveYear(2019);
    id.setMemberNumber(1234);
    Address a = m.getAddress();
    a.setAddress("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    m.setInitials("T.");
    m.setLastName("Tester");
    m.setEmail("tester@Test.nl");
    return am;
  }

}
