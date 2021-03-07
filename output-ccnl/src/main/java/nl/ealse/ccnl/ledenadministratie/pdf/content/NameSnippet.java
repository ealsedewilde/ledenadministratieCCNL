package nl.ealse.ccnl.ledenadministratie.pdf.content;

import nl.ealse.ccnl.ledenadministratie.model.Member;

public class NameSnippet implements ContentSnippet {

  private static final String CDATA = "<![CDATA[%s]]>";

  @Override
  public String getSnippet(Member member) {
    if (member.hasFirstName()) {
      return String.format(CDATA, member.getInitials());
    }
    return String.format(CDATA, member.getFullName());
  }

  @Override
  public String toString() {
    return "?name?";
  }

}
