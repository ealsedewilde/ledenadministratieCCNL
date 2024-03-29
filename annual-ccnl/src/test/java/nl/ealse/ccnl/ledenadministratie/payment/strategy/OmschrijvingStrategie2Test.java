package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Element;

class OmschrijvingStrategie2Test {
  
  private OmschrijvingStrategie sut = new OmschrijvingStrategie();
  
  private IngBooking booking = new TestBooking();
  
  @Test
  void testStrategie() {
    sut.bepaalLidnummer(booking);
    Assertions.assertEquals(1499, booking.getLidnummer());
  }
  
  private static class TestBooking extends IngBooking {

    public TestBooking() {
      super(Mockito.mock(Element.class));
    }
    
    @Override
    public String getOmschrijving() {
      return "Lidnummer 1499 1-4-21 tot 31-3-22";
    }
    
  }

}
