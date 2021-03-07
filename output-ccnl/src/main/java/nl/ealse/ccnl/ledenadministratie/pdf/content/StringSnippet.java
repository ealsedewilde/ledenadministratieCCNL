package nl.ealse.ccnl.ledenadministratie.pdf.content;

import nl.ealse.ccnl.ledenadministratie.model.Member;

public class StringSnippet implements ContentSnippet {

  private final String snippet;

  public StringSnippet(String snippet) {
    this.snippet = snippet + "\n";
  }

  @Override
  public String getSnippet(Member member) {
    return snippet;
  }

  @Override
  public String toString() {
    return snippet;
  }

}
