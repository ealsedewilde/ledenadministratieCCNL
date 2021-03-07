package nl.ealse.ccnl.ledenadministratie.payment.filter;

import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public interface Filter {

  /**
   * Filter om te bepalen op een booeking betrekking heeft op contributie.
   * Uitgangspunt: het is een contributie tenzij anders is bewezen.
   * Ieder filter checkt een boeking op een eigenschap. 
   * Wordt deze NIET gevonden dan is het geen contributie en heeft verder filteren geen zin.
   * @param booking - de mogelijke contributie
   * @return <code>true</code> als het volgende filter toegepast moet worden.
   */
  boolean doFilter(IngBooking booking);

}
