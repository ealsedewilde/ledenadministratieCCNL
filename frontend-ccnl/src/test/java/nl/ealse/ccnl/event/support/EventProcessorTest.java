package nl.ealse.ccnl.event.support;

public class EventProcessorTest {
  
  //@Test
  void testinitialize() {
    EventProcessor sut = EventProcessor.getInstance();
    sut.initialize();
    sut.toString();
  }

}
