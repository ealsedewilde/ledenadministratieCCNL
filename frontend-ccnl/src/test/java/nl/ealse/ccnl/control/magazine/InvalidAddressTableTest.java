package nl.ealse.ccnl.control.magazine;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidAddressTableTest extends FXMLBaseTest {
  
  private static MemberService service;
  
  private InvalidAdressTable sut;
  
  @Test
  void testController() {
    Assertions.assertTrue(runFX(() -> {
      prepare();
      doTest();
  }));

  }
  
  private void doTest() {
    sut.invalidAddressTable();
  }

  
  private void prepare() {
    List<Member> members = new ArrayList<>();
    members.add(getMember());
    service = ApplicationContext.getComponent(MemberService.class);
    reset(service);
    when(service.findMembersWithInvalidAddress()).thenReturn(members);
    sut = getTestSubject(InvalidAdressTable.class);
    getPageWithFxController(sut, PageName.MAGAZINE_INVALID_ADDRESS_TABLE);

  }

  private Member getMember() {
    Member m = new Member();
    m.setInitials("T.");
    m.setLastNamePrefix("de");
    m.setLastName("Tester");
    Address a = m.getAddress();
    a.setStreet("Straat");
    a.setAddressNumber("1");
    a.setPostalCode("1234 AA");
    a.setCity("Plaats");
    a.setAddressInvalid(true);
    m.setMemberNumber(4999);
    return m;
  }

}
