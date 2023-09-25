package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {

  @Column(name = "address", nullable = false)
  private String street;
  @Column(nullable = false)
  private String addressNumber;
  private String addressNumberAppendix;
  private String postalCode;
  @Column(nullable = false)
  private String city;
  private String country;

  /**
   * A magazine could not be delivered on this Address.
   */
  private boolean addressInvalid;

  public String getStreetAndNumber() {
    StringBuilder sb = new StringBuilder();
    sb.append(street).append(" ");
    sb.append(addressNumber);
    if (addressNumberAppendix != null) {
      sb.append(addressNumberAppendix);
    }
    return sb.toString();
  }

}
