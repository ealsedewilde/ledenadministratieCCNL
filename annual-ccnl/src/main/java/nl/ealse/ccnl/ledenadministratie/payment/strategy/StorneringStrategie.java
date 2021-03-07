package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

/**
 * Strategie voor het vinden van het lid bij een stornering.
 * @author ealse
 *
 */
public class StorneringStrategie implements LidnummerStrategie {

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    if (booking.isStornering()) {
      String info = booking.getStorneringInfo().substring(4);
      booking.setLidnummer(Integer.parseInt(info));
    }
  }

}
