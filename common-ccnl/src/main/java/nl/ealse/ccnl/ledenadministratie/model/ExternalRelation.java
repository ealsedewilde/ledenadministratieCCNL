package nl.ealse.ccnl.ledenadministratie.model;

import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import lombok.Data;

@Entity
@Inheritance
@DiscriminatorColumn(name = "RELATION_TYPE")
@Data
public class ExternalRelation implements AddressOwner {

  @Id
  private Integer relationNumber;

  @Basic(optional = true)
  private String relationName;
  @Basic(optional = false)
  private String contactName;
  private String contactNamePrefix = "t.a.v.";
  private String email;
  private String telephoneNumber;
  @Lob
  private String relationInfo;
  private LocalDate relationSince;
  private LocalDate modificationDate;
  @Embedded
  private Address address = new Address();

  @PrePersist
  public void prePersist() {
    modificationDate = LocalDate.now();
  }

  public int getId() {
    return relationNumber;
  }

}
