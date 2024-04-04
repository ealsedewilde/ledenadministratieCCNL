package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
/**
 * Checken of de naam uit de boeking voorkomt ind de lijst met active leden.
 *
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
    List<Member> hits = new ArrayList<>();
    String naam = booking.getNaam().toUpperCase();
    String[] parts = naam.split(" ");
    for (int ix = 0; ix < parts.length; ix++) {
      String part = parts[ix];
      members.forEach(m -> {
        if (m.getLastName().equalsIgnoreCase(part)) {
          hits.add(m);
        }
      });
    }
    
    if (hits.isEmpty()) {
      return;
    }
    if (hits.size() == 1) {
      booking.setLidnummer(hits.get(0).getMemberNumber());
      return;
    } 
      
    matchOnInitials(booking, hits, parts);
  }

  private void matchOnInitials(IngBooking booking, List<Member> hits, String[] parts) {
    // Nu de matches controleren op voorletters.
    for (Iterator<Member> itr = hits.iterator(); itr.hasNext();) {
      Member m = itr.next();
      StringBuilder sb = new StringBuilder();
      for (int ix = 0; ix < parts.length; ix++) {
        if (sb.length() > 0 && m.getLastName().equalsIgnoreCase(parts[ix])) {
          break;
        }
        if (parts[ix].length() == 1) {
          sb.append(parts[ix]).append('.');
        }
      }
      if (!sb.toString().equalsIgnoreCase(m.getInitials())) {
        // voorletters komen niet overeen
        itr.remove();
      }
    }

    if (hits.size() == 1) {
      booking.setLidnummer(hits.get(0).getMemberNumber());
    }
  }
  
}
