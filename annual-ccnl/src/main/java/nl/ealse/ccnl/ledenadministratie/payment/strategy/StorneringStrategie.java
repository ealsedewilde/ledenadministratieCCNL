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
      String stornoInfo = booking.getStorneringInfo();
      if (stornoInfo.length() > 3) {
        booking.setLidnummer(Integer.parseInt(stornoInfo.substring(4)));
      }
    }
  }

}
