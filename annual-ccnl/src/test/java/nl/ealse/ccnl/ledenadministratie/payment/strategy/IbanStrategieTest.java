package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IbanStrategieTest extends FilterTestBase {

  @Test
  void ibanStrategyTest() {
    List<Member> members = new ArrayList<>();
    Member m = new Member();
    m.setMemberNumber(1507);
    m.setLastName("Test2");
    m.getAddress().setPostalCode("1234 AA");
    m.getAddress().setStreet("Straat");
    m.setIbanNumber("GB33BUKB20201555555555");
    members.add(m);
    IbanStrategie sut = new IbanStrategie(members);
    Iterator<IngBooking> itr = init();
    IngBooking b = itr.next();
    sut.bepaalLidnummer(b);
    Assertions.assertEquals(1507, b.getLidnummer());
  }
}
