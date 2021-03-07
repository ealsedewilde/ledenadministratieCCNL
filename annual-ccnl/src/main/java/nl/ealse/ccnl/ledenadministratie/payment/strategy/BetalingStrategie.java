package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

/**
 * Strategie voor het vinden van het lid bij een ontvangen contributie.
 * @author ealse
 *
 */
@Slf4j
public abstract class BetalingStrategie implements LidnummerStrategie {
  
  @Getter
  private final List<Integer> nummers;
  
  protected BetalingStrategie(List<Integer> nummers) {
    this.nummers = nummers;
   }
  
  protected void logResult(IngBooking booking) {
    if (nummers.size() == 1) {
      log.debug(String.format("lid %s bij naam %s", nummers.get(0), booking.getNaam()));
      booking.setLidnummer(nummers.get(0));
    } else if (nummers.size() > 1) {
      String msg = String.format("%s resultaten bij naam %s", nummers.size(), booking.getNaam());
      log.warn(msg);
    }
  }

}
