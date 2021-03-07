package nl.ealse.ccnl.ledenadministratie.model;

import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class MemberBase implements AddressOwner {

  @Basic(optional = false)
  private String initials;
  private String lastNamePrefix;
  @Basic(optional = false)
  private String lastName;

  @Embedded
  private Address address = new Address();

  private String email;
  private String telephoneNumber;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private MembershipStatus memberStatus = MembershipStatus.ACTIVE;

  private String ibanNumber;
  private String ibanOwner;
  private LocalDate directDebitMandate;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;
  private LocalDate paymentDate;
  private boolean currentYearPaid;
  private boolean membercardIssued;
  @Lob
  private String paymentInfo;

  private boolean noMagazine;

  @Lob
  private String memberInfo;
  private LocalDate memberSince;
  private LocalDate modificationDate;

}
