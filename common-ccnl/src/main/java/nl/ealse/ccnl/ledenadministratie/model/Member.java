package nl.ealse.ccnl.ledenadministratie.model;

import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Member extends MemberBase {

  @Id
  private Integer memberNumber;
  
  @Transient
  private String ibanOwnerName;

  // CascadeType.REMOVE is inefficient, but there at most a just documents in the list
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
    if (!getFullName().equals(ibanOwnerName)) {
      setIbanOwner(ibanOwnerName);
    }
  }

  public String getFullName() {
    StringJoiner sj = new StringJoiner(" ");
    sj.add(getInitials());
    if (getLastNamePrefix() != null) {
      sj.add(getLastNamePrefix());
    }
    sj.add(getLastName());
    return sj.toString();
  }

  @PrePersist
  public void prePersist() {
    setModificationDate(LocalDate.now());
  }

}
