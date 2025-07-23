package nl.ealse.ccnl.event.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class EventPublisherTest {
  
  @Test
  void testInitialize() {
    EventPublisher.EventRegistryLoader registry = new EventPublisher.EventRegistryLoader();
    registry.call();
    String result = registry.toString();
    log.info(result);
    assertEquals(10671, result.length());
  }

}
