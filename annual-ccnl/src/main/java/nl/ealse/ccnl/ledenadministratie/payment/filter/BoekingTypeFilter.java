package nl.ealse.ccnl.ledenadministratie.payment.filter;

import nl.ealse.ccnl.ledenadministratie.payment.BookingType;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class BoekingTypeFilter implements Filter {

  @Override
  public boolean doFilter(IngBooking booking) {
    BookingType type = booking.getTypebooking();
    switch (type) {
      case ICDT:
      case RCDT:
      case RRCT:
      case IDDT:
        return true;
      default:
        return false;
    }
  }

}
