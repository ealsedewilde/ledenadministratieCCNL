package nl.ealse.ccnl.ledenadministratie.model;

import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;

/**
 * Text template for generating letters or mail.
 * 
 * @author ealse
 *
 */
@Entity
@Data
public class DocumentTemplate {

  @EmbeddedId
  private DocumentTemplateID templateID;

  @Lob
  @Basic(optional = false)
  private String template;

  @Basic(optional = false)
  private boolean includeSepaForm;

  @Basic(optional = false)
  private LocalDate modificationDate = LocalDate.now();

  @PrePersist
  @PreUpdate
  public void prePersist() {
    modificationDate = LocalDate.now();
  }

  public String getName() {
    return templateID.getName();
  }

  public String getDescription() {
    return templateID.getDocumentTemplateType().getDescription();
  }

}
