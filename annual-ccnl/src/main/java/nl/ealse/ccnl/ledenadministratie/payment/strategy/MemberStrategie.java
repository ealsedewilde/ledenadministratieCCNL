package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
/**
 * Checken of de naam uit de boeking voorkomt ind de lijst met active leden.
 * @author ealse
 *
 */
public class MemberStrategie implements LidnummerStrategie{
  private final List<Member> members;
  
  public MemberStrategie(List<Member> members) {
    this.members = members;
  }

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    String naam = booking.getNaam().toUpperCase();
    for (Member m : members) {
      int ix = naam.indexOf(m.getLastName().toUpperCase());
      if (ix != -1) {
        booking.setLidnummer(m.getMemberNumber());
      }
    }
    
  }

}
