package nl.ealse.ccnl.ledenadministratie.model;

import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class InternalRelation implements AddressOwner {

  @Id
  private Integer relationNumber;

  @Column(nullable = false, unique = true)
  private String title;

  @Basic(optional = false)
  private String contactName;
  private String telephoneNumber;
  private LocalDate modificationDate;

  @Embedded
  private Address address = new Address();

  public int getId() {
    return relationNumber;
  }

  @PrePersist
  public void prePersist() {
    setModificationDate(LocalDate.now());
  }


}
