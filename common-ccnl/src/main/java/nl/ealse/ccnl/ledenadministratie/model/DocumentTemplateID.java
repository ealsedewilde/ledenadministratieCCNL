package nl.ealse.ccnl.ledenadministratie.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
