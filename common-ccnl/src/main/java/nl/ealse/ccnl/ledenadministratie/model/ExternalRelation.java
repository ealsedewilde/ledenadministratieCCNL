package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Basic;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
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
  @PreUpdate
  public void prePersist() {
    modificationDate = LocalDate.now();
  }

  public int getId() {
    return relationNumber;
  }
  
  public String getFullName() {
    return relationName;
  }

}
