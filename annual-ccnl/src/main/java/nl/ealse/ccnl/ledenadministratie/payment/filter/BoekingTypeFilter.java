package nl.ealse.ccnl.ledenadministratie.payment.filter;

import static nl.ealse.ccnl.ledenadministratie.payment.BookingType.*;

import java.util.Arrays;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.payment.BookingType;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class BoekingTypeFilter implements Filter {

  private static final List<BookingType> TYPES = Arrays.asList(ICDT, IRCT, RCDT, RRCT, IDDT);

  @Override
  public boolean doFilter(IngBooking booking) {
    BookingType type = booking.getTypebooking();
    return TYPES.contains(type);
  }

}
