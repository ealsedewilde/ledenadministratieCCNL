package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documentSeq")
  @SequenceGenerator(name = "documentSeq", sequenceName = "DOCUMENT_SEQ", allocationSize = 10)
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
