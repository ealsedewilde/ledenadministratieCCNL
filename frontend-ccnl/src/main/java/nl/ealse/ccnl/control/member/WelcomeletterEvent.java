package nl.ealse.ccnl.control.member;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import org.springframework.context.ApplicationEvent;

/**
 * Trigger sending a welcome letter for a new member.
 */
@SuppressWarnings("serial")
public class WelcomeletterEvent extends ApplicationEvent {
  
  @Getter
  private final transient Member member;

  public WelcomeletterEvent(Object source, Member member) {
    super(source);
    this.member = member;
  }

}
