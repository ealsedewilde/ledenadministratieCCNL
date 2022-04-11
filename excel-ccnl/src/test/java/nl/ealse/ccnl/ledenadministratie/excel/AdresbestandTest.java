package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.test.util.ExcelPropertiesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AdresbestandTest {
  
  @TempDir
  File tempDir;
  
  private Adresbestand sut;
  
  private CCNLColumnProperties ccnlColumnProperties;
  
  @Test
  void testAdrebestand() {
    ccnlColumnProperties = ExcelPropertiesFactory.newExcelProperties();
    File f = new File(tempDir, "adressenTest.xlsx");
    try {
      sut = new Adresbestand(f, ccnlColumnProperties);
      sut.addHeading();
      sut.addMember(member());
      sut.addClub(club());
      sut.addExternalRelation(relation());
      sut.addInternalRelation(functie());
      sut.close();
      long l = f.length();
      
      boolean ok = l > 3875 || l < 3880;
      Assertions.assertTrue(ok);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  private Member member() {
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
    return m;
  }
  
  private ExternalRelationClub club() {
    ExternalRelationClub r = new ExternalRelationClub();
    Address a = r.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    r.setContactName("Pietje Puk");
    r.setRelationNumber(8201);
    r.setContactNamePrefix("t.n.v");
    r.setModificationDate(LocalDate.of(2020,  12, 5));
    r.setEmail("info@test.nl");
    r.setTelephoneNumber("06-12345678");
    r.setRelationName("The Club");
    return r;
  }
  
  private ExternalRelation relation() {
    ExternalRelation r = new ExternalRelation();
    Address a = r.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    r.setContactName("Pietje Puk");
    r.setRelationNumber(8401);
    r.setContactNamePrefix("t.n.v");
    r.setModificationDate(LocalDate.of(2020,  12, 5));
    r.setEmail("info@test.nl");
    r.setTelephoneNumber("06-12345678");
    r.setRelationName("The Partner");
    return r;
  }
  
  private InternalRelation functie() {
    InternalRelation r = new InternalRelation();
    Address a = r.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    r.setContactName("Pietje Puk");
    r.setRelationNumber(8601);
    r.setTitle("functie");
    r.setModificationDate(LocalDate.of(2020,  12, 5));
    r.setTelephoneNumber("06-12345678");
    return r;
  }

}
