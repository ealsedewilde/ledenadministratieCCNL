package nl.ealse.ccnl.control.member;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import org.springframework.context.ApplicationEvent;

/**
 * Trigger mail handling in case of a canceled memebership.
 */
@SuppressWarnings("serial")
public class CancelMailEvent extends ApplicationEvent {
  
  @Getter
  private final Member member;

  public CancelMailEvent(Object source, Member member) {
    super(source);
    this.member = member;
  }

}
