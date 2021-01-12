package nl.ealse.ccnl.ledenadministratie.model;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ArchivedMember {

  @EmbeddedId
  private ArchiveId id;

  @Embedded
  private MemberBase member;

}
