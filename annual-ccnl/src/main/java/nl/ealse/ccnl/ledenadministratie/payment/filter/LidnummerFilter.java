package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.strategy.LidnummerBepaling;

@Slf4j
public class LidnummerFilter implements Filter {

  private final LidnummerBepaling lidnummerBepaling;

  public LidnummerFilter(List<Member> members) {
    this.lidnummerBepaling = new LidnummerBepaling(members);
  }

  @Override
  public boolean doFilter(IngBooking booking) {
    lidnummerBepaling.bepaalLidnummer(booking);
    if (booking.getLidnummer() == 0) {
      log.warn("Geen lidnummer te bepalen voor " + booking.getNaam());
      return false;
    }
    log.debug("Lidnummer: " + booking.getLidnummer());
    return true;
  }

}
