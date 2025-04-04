package nl.ealse.ccnl.ledenadministratie.payment.filter;

import static nl.ealse.ccnl.ledenadministratie.payment.BookingType.IDDT;
import static nl.ealse.ccnl.ledenadministratie.payment.BookingType.RCDT;
import static nl.ealse.ccnl.ledenadministratie.payment.BookingType.RRCT;
import java.util.Arrays;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.payment.BookingType;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class BoekingTypeFilter implements Filter {

  private static final List<BookingType> TYPES = Arrays.asList(RCDT, RRCT, IDDT);

  @Override
  public boolean doFilter(IngBooking booking) {
    BookingType type = booking.getTypebooking();
    if (BookingType.RCDT == type && "BOOK".equals(booking.getTypebookingSub())) {
      // eigen periodieke overschrijvingen uitsluiten
      return false;
    }
    if (BookingType.IDDT == type && "ESDD".equals(booking.getTypebookingSub())) {
      // Automatische incasso uitsluiten
      return false;
    }
    return TYPES.contains(type);
  }

}
