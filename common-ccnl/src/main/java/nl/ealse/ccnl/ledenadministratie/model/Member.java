package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Member extends MemberBase {

  @Id
  private Integer memberNumber;
  
  /**
   * Value as used in the frontend.
   * It is either the ibanOwner or the full name of the member.
   */
  @Transient
  private String ibanOwnerName;

  // CascadeType.REMOVE is inefficient, but there at most a just a few documents in the list
  @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
  private List<Document> documents;

  public int getId() {
    return getMemberNumber();
  }

  public boolean hasFirstName() {
    return getInitials() != null && getInitials().indexOf('.') == -1;
  }

  public String getIbanOwnerName() {
    if (getIbanOwner() == null) {
      ibanOwnerName = getFullName();
    } else {
      ibanOwnerName = getIbanOwner();
    }
    return ibanOwnerName;
  }
  
  public void setIbanOwnerName(String ibanOwnerName) {
    this.ibanOwnerName = ibanOwnerName;
  }

  /**
   * Format the full name from its parts.
   * Initially the member is empty. In that case null is returned.
   * @return the full name or null
   */
  public String getFullName() {
    StringJoiner sj = new StringJoiner(" ");
    if (getInitials() != null) {
      sj.add(getInitials());
    }
    if (getLastNamePrefix() != null) {
      sj.add(getLastNamePrefix());
    }
    if (getLastName() != null) {
      sj.add(getLastName());
    }
    if (sj.length() > 0) {
      return sj.toString();
    }
    return null;
  }

  @PrePersist
  @PreUpdate
  public void prePersist() {
    String memberName = getFullName();
    if (memberName == null || !memberName.equals(ibanOwnerName)) {
      setIbanOwner(ibanOwnerName);
    }
    setModificationDate(LocalDate.now());
  }

}
