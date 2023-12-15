package nl.ealse.ccnl.control.member;

import java.util.EventObject;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Trigger sending a welcome letter for a new member.
 */
@SuppressWarnings("serial")
public class WelcomeletterEvent extends EventObject {
  
  @Getter
  private final transient Member member;

  public WelcomeletterEvent(Object source, Member member) {
    super(source);
    this.member = member;
  }

}
