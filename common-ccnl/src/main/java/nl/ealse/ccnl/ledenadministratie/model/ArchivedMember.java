package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.lang.reflect.InvocationTargetException;
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
  
  public ArchivedMember(MemberBase member) {
    try {
      BeanUtils.copyProperties(this, member);
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error("failed to populate this object", e);
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
