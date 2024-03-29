package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NaamStrategieTest extends FilterTestBase {

  @Test
  void bepaalLidnummerTest() {
    List<Member> members = new ArrayList<>();
    Member m = new Member();
    m.setMemberNumber(1507);
    m.setLastName("Test2");
    m.getAddress().setPostalCode("1234 AA");
    m.getAddress().setStreet("Straat");
    members.add(m);
    m = new Member();
    m.setMemberNumber(1599);
    m.setLastName("Getest2");
    m.getAddress().setPostalCode("1999 AA");
    m.getAddress().setStreet("Straat");
    members.add(m);

    Iterator<IngBooking> itr = init();

    NaamStrategie sut = new NaamStrategie(members);
    IngBooking b = itr.next();
    sut.bepaalLidnummer(b);
    b = itr.next();
    Assertions.assertEquals(0, b.getLidnummer());
    sut.bepaalLidnummer(b);
    Assertions.assertEquals(1507, b.getLidnummer());
    
    // Two equal scores => matching has to be done manually
    m = new Member();
    m.setMemberNumber(1600);
    m.setLastName("Test2");
    m.getAddress().setPostalCode("1999 AA");
    m.getAddress().setStreet("Straat");
    members.add(m);
    b.setLidnummer(0);
    sut.bepaalLidnummer(b);
    Assertions.assertEquals(0, b.getLidnummer());


  }

}
