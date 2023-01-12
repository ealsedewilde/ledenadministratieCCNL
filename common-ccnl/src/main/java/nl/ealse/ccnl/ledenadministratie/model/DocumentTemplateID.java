package nl.ealse.ccnl.ledenadministratie.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@SuppressWarnings("serial")
@Embeddable
@Data
public class DocumentTemplateID implements Serializable {

  @Basic(optional = false)
  private String name;

  @Enumerated(EnumType.STRING)
  private DocumentTemplateType documentTemplateType;


}
