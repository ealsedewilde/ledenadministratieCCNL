package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

  // CascadeType.REMOVE is inefficient, but there are at most just a few documents in the list
  @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
  private List<Document> documents;

  public boolean hasFirstName() {
    return getInitials() != null && getInitials().indexOf('.') == -1;
  }

  /**
   * The name associated with the IBAN-number.
   * By default it is the name of the member, but a different name can be provided via the user interface.
   * @return - the name associated with the IBAN-number
   */
  public String getIbanOwnerName() {
    if (getIbanOwner() == null) {
      return getFullName();
    }
    return getIbanOwner();
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

  //used in search results TableView
  public int getKey() {
    return memberNumber;
  }

  @PrePersist
  @PreUpdate
  public void prePersist() {
    setModificationDate(LocalDate.now());
  }

}
