package nl.ealse.ccnl.ledenadministratie.payment.filter;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

@Slf4j
public class OmschrijvingFilter implements Filter {

  private static final String[] KEYS = {"restitutie", "contributie", "lidnummer", "lidnr", "lid ", "lidmaat"};

  @Override
  public boolean doFilter(IngBooking booking) {
    if (!booking.isStornering()) {
      String omschrijving = booking.getOmschrijving().toLowerCase();
      for (String key : KEYS) {
        int ix = omschrijving.indexOf(key);
        if (ix != -1) {
          return true;
        }
      }
      try {
        Integer.parseInt(omschrijving.trim());
        return true;
      } catch (NumberFormatException nfe) {
        log.info(omschrijving);
        booking.setContributie(false);
        return false;
      }
    }
    // booking is 'storno'
    return true;
  }

}
