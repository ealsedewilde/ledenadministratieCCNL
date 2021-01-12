package nl.ealse.ccnl.ledenadministratie.payment.filter;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public interface Filter {

  boolean doFilter(IngBooking booking);

}
