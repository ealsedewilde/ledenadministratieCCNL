package nl.ealse.ccnl.control.member;

import java.util.EventObject;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Trigger mail handling in case of a canceled memebership.
 */
@SuppressWarnings("serial")
public class CancelMailEvent extends EventObject {

  @Getter
  private final transient Member member;

  public CancelMailEvent(Object source, Member member) {
    super(source);
    this.member = member;
  }

}
