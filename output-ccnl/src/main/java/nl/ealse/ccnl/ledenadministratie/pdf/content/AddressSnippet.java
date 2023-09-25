package nl.ealse.ccnl.ledenadministratie.pdf.content;

import java.util.StringJoiner;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public class AddressSnippet implements ContentSnippet {

  private static final String CDATA = "<![CDATA[%s]]>";

  @Override
  public String getSnippet(Member member) {
    StringJoiner sj = new StringJoiner("\n");
    Address address = member.getAddress();
    sj.add(String.format(CDATA, member.getFullName()));
    sj.add(String.format(CDATA, address.getStreetAndNumber()));
    sj.add(address.getPostalCode() + "  " + address.getCity());
    if (address.getCountry() != null) {
      sj.add(address.getCountry());
    }
    return sj.toString() + "\n";
  }

  @Override
  public String toString() {
    return "?address?";
  }


}
