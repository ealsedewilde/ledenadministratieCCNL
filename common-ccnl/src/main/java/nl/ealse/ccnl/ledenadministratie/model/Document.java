package nl.ealse.ccnl.ledenadministratie.model;

import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "OWNER_ID")
  private Member owner;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private DocumentType documentType;

  @Basic(optional = false)
  private String documentName;

  private String description;

  @Basic(optional = false)
  @Lob
  private byte[] pdf;

  @Basic(optional = false)
  private LocalDate creationDate;

  @PrePersist
  @PreUpdate
  public void prePersist() {
    creationDate = LocalDate.now();
  }

}
