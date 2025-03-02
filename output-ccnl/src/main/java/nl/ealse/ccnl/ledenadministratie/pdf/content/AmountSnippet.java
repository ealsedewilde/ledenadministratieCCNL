package nl.ealse.ccnl.ledenadministratie.pdf.content;

import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.util.AmountToPay;

public class AmountSnippet implements ContentSnippet {

  @Override
  public String toString() {
    return "?amount?";
  }

  @Override
  public String getSnippet(Member member) {
    return AmountToPay.amountToPayAsString(member.getAmountPaid());
  }

}
