package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class LidnummerBepaling {

  private final List<LidnummerStrategie> filters = new ArrayList<>();

  private static List<Integer> nummers = new ArrayList<>();

  public LidnummerBepaling(List<Member> members) {
    filters.add(new StorneringStrategie());
    filters.add(new NaamStrategie(nummers, members));
    filters.add(new OmschrijvingStrategie(nummers));
    filters.add(new AdresStrategie(members));
    filters.add(new IbanStrategie(members));
  }

  public void bepaalLidnummer(IngBooking booking) {
    nummers.clear();
    for (LidnummerStrategie strategie : filters) {
      if (booking.getLidnummer() > 0) {
        break;
      }
      strategie.bepaalLidnummer(booking);
    }
  }

}
