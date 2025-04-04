package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LidnummerFilterTest extends FilterTestBase {

  @Test
  void filterTest() {
    List<Member> members = new ArrayList<>();
    Member m = new Member();
    m.setMemberNumber(1507);
    m.setLastName("Tester");
    m.getAddress().setPostalCode("1234 AA");
    m.getAddress().setStreet("Straat");
    members.add(m);
    LidnummerFilter filter = new LidnummerFilter(new ArrayList<String>(), members);
    Iterator<IngBooking> itr = init();
    IngBooking b = itr.next();
    filter.doFilter(b);
    Assertions.assertEquals(1507, b.getLidnummer());

  }

}
