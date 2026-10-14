package nl.ealse.ccnl.control.magazine;

import java.util.StringJoiner;
import lombok.Value;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Data of an invalid address for the Invalid Address Overview.
 */
@Value
public class InvalidAddressData {

  private Integer memberNumber;
  
  private String invalidAddress;
  
  private String reason;
  
  /**
   * Construct an instance from Member properties.
   *
   * @param member member with invalid address
   */
  public InvalidAddressData(Member member) {
    this.memberNumber = member.getMemberNumber();
    this.invalidAddress = buildAddress(member);
    this.reason =  member.getMemberInfo();
  }
  
  private String buildAddress(Member member) {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add(member.getFullName());
    Address address = member.getAddress();
    joiner.add(address.getStreetAndNumber());
    
    StringJoiner sj = new StringJoiner(" ");
    String pc = address.getPostalCode();
    if (pc != null && !address.getPostalCode().isBlank()) {
      sj.add(pc);
    }
    sj.add(address.getCity());
    String country = address.getCountry();
    if (country != null && !country.isBlank()) {
      sj.add(country);
    }
    joiner.add(sj.toString());
    
    return joiner.toString();
    
  }

}
