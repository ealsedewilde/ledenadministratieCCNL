package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
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

  private boolean noMagazine;


  @Embedded
  private Address address = new Address();

  public int getId() {
    return relationNumber;
  }

  @PrePersist
  @PreUpdate
  public void prePersist() {
    setModificationDate(LocalDate.now());
  }
  
  public String getFullName() {
    return title;
  }



}
