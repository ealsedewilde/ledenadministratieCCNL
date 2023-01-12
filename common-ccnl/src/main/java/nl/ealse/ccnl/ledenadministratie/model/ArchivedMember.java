package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ArchivedMember {

  @EmbeddedId
  private ArchiveId id;

  @Embedded
  private MemberBase member;

}
