package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class ArchivedMember extends MemberBase {

  @EmbeddedId
  private ArchiveId id;
  
  public ArchivedMember() {
    
  }
  
  public ArchivedMember(Member member) {
    this.id = new ArchiveId();
    this.id.setArchiveYear(LocalDate.now().getYear());
    this.id.setMemberNumber(member.getMemberNumber());

    try {
      BeanUtils.copyProperties(this, member);
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error("failed to populate this object", e);
      throw new RuntimeException(e);
    }
  }

}
