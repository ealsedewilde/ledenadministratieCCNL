package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class LidnummerBepaling {

  private final List<LidnummerStrategie> filters = new ArrayList<>();

  public LidnummerBepaling(List<Member> members) {
    filters.add(new StorneringStrategie());
    
    // Onderstaande filters worden toegepast als het geen stornering betreft
    filters.add(new OmschrijvingStrategie());
    filters.add(new NaamStrategie(members));
    filters.add(new AdresStrategie(members));
    filters.add(new IbanStrategie(members));
    filters.add(new NummerStrategie());
    filters.add(new MemberStrategie(members));
  }

  public void bepaalLidnummer(IngBooking booking) {
    for (LidnummerStrategie strategie : filters) {
      if (booking.getLidnummer() > 0) {
        break;
      }
      strategie.bepaalLidnummer(booking);
    }
  }

}
