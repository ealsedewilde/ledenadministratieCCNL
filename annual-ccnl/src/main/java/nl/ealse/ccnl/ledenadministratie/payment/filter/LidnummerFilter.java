package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.strategy.LidnummerBepaling;

/**
 * Het laatste toe te passen filter.
 * Hierbij wordt geprobeerd het lidnummer te koppelen aan een boeking.
 * Dit gebeurt op basis van een aantal benaderingen.
 * @author ealse
 *
 */
@Slf4j
public class LidnummerFilter implements Filter {

  private final List<String> messageList;
  private final LidnummerBepaling lidnummerBepaling;

  public LidnummerFilter(List<String> messageList, List<Member> members) {
    this.messageList = messageList;
    this.lidnummerBepaling = new LidnummerBepaling(members);
  }

  @Override
  public boolean doFilter(IngBooking booking) {
    lidnummerBepaling.bepaalLidnummer(booking);
    if (booking.getLidnummer() == 0) {
      String msg = String.format("Geen lidnummer te bepalen voor %s (%s)", 
          booking.getNaam(),
          booking.getOmschrijving());
      log.warn(msg);
      messageList.add(msg);
      return false;
    }
    log.debug("Lidnummer: " + booking.getLidnummer());
    return true;
  }

}
