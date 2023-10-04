package nl.ealse.ccnl.control.member;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class IbanNumberAddedEvent extends ApplicationEvent {

  public IbanNumberAddedEvent(Object source) {
    super(source);
  }

}
