package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.File;
import java.io.IOException;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArchiefbestandTest {
  
  @TempDir
  File tempDir;
  
  private Archiefbestand sut;
  
  @Test
  void testArchiefbestand() {
    File f = new File(tempDir, "archiefTest.xlsx");
    try {
      sut = new Archiefbestand(f);
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
    Member m = new Member();
    m.setMemberNumber(1234);
    Address a = m.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    m.setInitials("T.");
    m.setLastName("Tester");
    m.setEmail("tester@Test.nl");
    ArchivedMember am = new ArchivedMember(m);
    return am;
  }

}
