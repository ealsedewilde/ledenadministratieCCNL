package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AdresStrategieTest extends FilterTestBase {

  @Test
  void bepaalAddressTest() {
    List<Member> members = new ArrayList<>();
    Member m = new Member();
    m.setMemberNumber(1000);
    m.setLastName("Test1");
    m.getAddress().setPostalCode("1234 AA");
    m.getAddress().setAddress("Straat");
    members.add(m);

    Iterator<IngBooking> itr = init();

    AdresStrategie sut = new AdresStrategie(members);
    IngBooking b = itr.next();
    String s = b.getAdres();
    Assertions.assertEquals("Straat 10", s);
    sut.bepaalLidnummer(b);
    Assertions.assertEquals(1000, b.getLidnummer());
  }

}
