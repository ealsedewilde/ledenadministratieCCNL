package nl.ealse.ccnl.event.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class EventProcessorTest {
  
  @Test
  void testInitialize() {
    EventProcessor eventProcessor = EventProcessor.getInstance();
    eventProcessor.initialize();
    String result = eventProcessor.toString();
    log.info(result);
    assertEquals(10671, result.length());
  }

}
