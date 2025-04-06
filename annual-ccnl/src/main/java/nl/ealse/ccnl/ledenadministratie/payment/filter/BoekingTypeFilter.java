package nl.ealse.ccnl.ledenadministratie.payment.filter;

import nl.ealse.ccnl.ledenadministratie.payment.BookingType;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class BoekingTypeFilter implements Filter {

  /*
   * De boeking is mogelijk interressant als: 
   * IRCT = terugbetaling contributie aan lid; 
   * RCDT/ESCT= periodieke overboeking door lid; 
   * RRCT = eenmalige overboeking door lid; 
   * IDDT/UPDD = stornering
   */
  @Override
  public boolean doFilter(IngBooking booking) {
    BookingType type = booking.getTypebooking();
    switch(type) {
      case RCDT:
        // Interne boeking (b.v. vanaf spaarrekening) niet meenemen
        return !"BOOK".equals(booking.getTypebookingSub());
      case IDDT:
        // Automatische incasso alleen stornering meenemen)
        return "UPDD".equals(booking.getTypebookingSub());
      case IRCT, RRCT:
        return true;
      default:
        return false;
        
    }
 }

}
