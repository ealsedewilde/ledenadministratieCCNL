package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public class IbanStrategie extends BetalingStrategie {
  
  private final List<Member> members;

  public IbanStrategie(List<Member> members) {
    super(new ArrayList<>());
    this.members = members;
  }

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    getNummers().clear();
    String iban = booking.getTegenRekening();
    for (Member member : members) {
      if (iban.equals(member.getIbanNumber())) {
        getNummers().add(member.getMemberNumber());
      }
    }
    logResult(booking);
  }

}
