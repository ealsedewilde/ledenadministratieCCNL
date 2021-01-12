package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public interface LidnummerStrategie {

  void bepaalLidnummer(IngBooking booking);

}
