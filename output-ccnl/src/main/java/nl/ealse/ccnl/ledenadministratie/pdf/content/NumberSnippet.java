package nl.ealse.ccnl.ledenadministratie.pdf.content;

import nl.ealse.ccnl.ledenadministratie.model.Member;

public class NumberSnippet implements ContentSnippet {

  @Override
  public String toString() {
    return "?number?";
  }

  @Override
  public String getSnippet(Member member) {
    return Integer.toString(member.getMemberNumber());
  }


}
