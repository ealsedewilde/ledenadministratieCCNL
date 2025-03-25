package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDate;
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
  private String bicCode;
  private String ibanOwner;
  private LocalDate directDebitMandate;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;
  private LocalDate paymentDate;
  private boolean directDebitExecuted;
  private boolean currentYearPaid;
  private boolean membercardIssued;
  @Lob
  private String paymentInfo;
  @Column (precision = 5, scale = 2 )
  private BigDecimal amountPaid = BigDecimal.ZERO;

  private boolean noMagazine;

  @Lob
  private String memberInfo;
  private LocalDate memberSince;
  private LocalDate modificationDate;
  
  public void setPaymentInfo(String info) {
    if (info == null) {
      paymentInfo = null;
    } else if (paymentInfo == null) {
      paymentInfo = info;
    } else {
      paymentInfo = String.join(", ", paymentInfo, info);
    }
    
  }

}
