package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

@Slf4j
public class IbanStrategie implements LidnummerStrategie {
  private final List<Integer> nummers = new ArrayList<>();
  private final List<Member> members;

  public IbanStrategie(List<Member> members) {
    this.members = members;
  }

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    if (booking.getLidnummer() > 0) {
      return;
    }
    String iban = booking.getTegenRekening();
    nummers.clear();
    for (Member member : members) {
      if (iban.equals(member.getIbanNumber())) {
        nummers.add(member.getMemberNumber());
      }
    }
    if (nummers.size() == 1) {
      log.debug(String.format("lid %s bij naam %s", nummers.get(0), booking.getNaam()));
      booking.setLidnummer(nummers.get(0));
    } else {
      log.debug(String.format("%s resultaten bij naam %s", nummers.size(), booking.getNaam()));
    }
  }

}
