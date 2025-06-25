package nl.ealse.ccnl.event.support;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventPublisher {

  private final EventProcessor eventProcessor;

  static {
    eventProcessor = EventProcessor.getInstance();
  }

  public void publishEvent(Object event) {
    eventProcessor.processEvent(event);
  }

}
