package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class AdresStrategie extends BetalingStrategie {
  private final List<Member> members;

  public AdresStrategie(List<Member> members) {
    super(new ArrayList<>());
    this.members = members;
  }

  /**
   * 
   */
  // Could it be that we no longer get address info from the ING?
  // Perhaps due to new privacy regulations?
  @Override
  public void bepaalLidnummer(IngBooking booking) {
    getNummers().clear();
    final String pc1 = booking.getPostcode();
    for (Member member : members) {
      if (pc1.indexOf(member.getAddress().getPostalCode().replaceAll("\\s", "")) > -1
          || booking.getAdres().indexOf(member.getAddress().getAddress()) > -1) {
        getNummers().add(member.getMemberNumber());
        break;
      }

    }
    logResult(booking);
  }


}
