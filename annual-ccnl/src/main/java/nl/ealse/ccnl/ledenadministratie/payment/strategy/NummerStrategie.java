package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class NummerStrategie implements LidnummerStrategie {

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    String description = booking.getOmschrijving();
    try {
      int nummer = Integer.parseInt(description);
      booking.setLidnummer(nummer);
    } catch (NumberFormatException nfe) {
      // geen actie nodig.
    }
  }

}
