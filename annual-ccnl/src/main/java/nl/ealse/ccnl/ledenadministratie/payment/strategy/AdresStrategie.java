package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

@Slf4j
public class AdresStrategie implements LidnummerStrategie {
  private final List<Member> members;

  public AdresStrategie(List<Member> members) {
    this.members = members;
  }

  /**
   * 
   */
  // Could it be that we no longer get address info from the ING?
  // Perhaps due to new privacy regulations?
  @Override
  public void bepaalLidnummer(IngBooking booking) {
    if (booking.getLidnummer() > 0) {
      return;
    }
    int nummer = 0;
    final String pc1 = booking.getPostcode();
    for (Member member : members) {
      if (pc1.indexOf(member.getAddress().getPostalCode().replaceAll("\\s", "")) > -1
          || booking.getAdres().indexOf(member.getAddress().getAddress()) > -1) {
        nummer = member.getMemberNumber();
        break;
      }

    }
    if (nummer > 0) {
      booking.setLidnummer(nummer);
      log.debug(String.format("lid %s bij naam %s", nummer, booking.getNaam()));
    }
  }


}
