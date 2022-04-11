package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Element;

class NaamStrategie2Test {
  
  private List<Member> members = new ArrayList<>();
  private Member member = new Member();
  
  private NaamStrategie sut = new NaamStrategie(members);
  
  private IngBooking booking = new TestBooking();
  
  @Test
  void testNaam() {
    members.add(member);
    member.setLastName("Essen");
    member.setMemberNumber(1499);
    sut.bepaalLidnummer(booking);
    Assertions.assertEquals(1499, booking.getLidnummer());
  }
  
  private static class TestBooking extends IngBooking {

    public TestBooking() {
      super(Mockito.mock(Element.class));
    }
    
    @Override
    public String getNaam() {
      return "ESSEN G R VAN";
    }
    
  }

}
