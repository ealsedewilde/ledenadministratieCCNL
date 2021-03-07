package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

/**
 * Strategie voor het vinden van het lid bij een boeking.
 * @author ealse
 */
public interface LidnummerStrategie {

  void bepaalLidnummer(IngBooking booking);

}
