package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Element;

class MemberStrategieTest {
  
  private List<Member> members = new ArrayList<>();
  private Member member = new Member();
  
  private MemberStrategie sut = new MemberStrategie(members);
  
  @Test
  void testNaam() {
    members.add(member);
    member.setLastName("Essen");
    member.setInitials("G.R.");
    member.setMemberNumber(1499);
    IngBooking booking = new TestBooking("ESSEN G R VAN");
    sut.bepaalLidnummer(booking);
    Assertions.assertEquals(1499, booking.getLidnummer());
  }
  @Test
  void testNaam2() {
    members.add(member);
    member.setLastName("Essen");
    member.setInitials("G.R.");
    member.setMemberNumber(1499);
    IngBooking booking = new TestBooking("HR G R VAN ESSEN EN/OFMW I EIKEN");
    sut.bepaalLidnummer(booking);
    Assertions.assertEquals(1499, booking.getLidnummer());
  }
  
  private static class TestBooking extends IngBooking {
    private final String naam;

    public TestBooking(String naam) {
      super(Mockito.mock(Element.class));
      this.naam = naam;
    }
    
    @Override
    public String getNaam() {
      return naam;
    }
    
  }



}
