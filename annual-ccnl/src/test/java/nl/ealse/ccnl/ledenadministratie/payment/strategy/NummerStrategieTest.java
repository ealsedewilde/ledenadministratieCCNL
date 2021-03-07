package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class NummerStrategieTest {
  
  private NummerStrategie sut = new NummerStrategie();
  
  @Test
  void testStrategie() {
    IngBooking booking = mock(IngBooking.class);
    when(booking.getOmschrijving()).thenReturn("1275");
    sut.bepaalLidnummer(booking);
    ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
    verify(booking).setLidnummer(argument.capture());
    Assertions.assertEquals(1275, argument.getValue());
  }

}
