package nl.ealse.ccnl.ledenadministratie.payment.filter;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class BedragFilter implements Filter {

  @Override
  public boolean doFilter(IngBooking booking) {
    double bedrag = booking.getBedrag();
    if (bedrag < 0) {
      bedrag = bedrag * -1;
    }
    return bedrag == 27.5 || bedrag == 30;
  }

}
