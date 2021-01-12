package nl.ealse.ccnl.ledenadministratie.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import lombok.Data;

@SuppressWarnings("serial")
@Embeddable
@Data
public class ArchiveId implements Serializable {

  @Basic(optional = false)
  private Integer memberNumber;

  @Basic(optional = false)
  private Integer archiveYear;


}
