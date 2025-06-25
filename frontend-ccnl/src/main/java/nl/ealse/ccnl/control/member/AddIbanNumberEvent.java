package nl.ealse.ccnl.control.member;

import java.util.function.Supplier;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Member;

@Getter
public class AddIbanNumberEvent {

  private final Member member;
  private final Supplier<Void> nextAction;

  public AddIbanNumberEvent(Member member, Supplier<Void> nextAction) {
    this.nextAction = nextAction;
    this.member = member;
  }

}
