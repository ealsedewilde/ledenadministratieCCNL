package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
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
