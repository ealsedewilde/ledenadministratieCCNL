package nl.ealse.ccnl.event.support;

import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.annual.ResetPaymentCommand;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventProcessorTest extends FXBase {
  
  private static PageController pc;
  private static ResetPaymentCommand cmd;
  
  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      testCommandCondition();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }
  
  void testCommandCondition() {
    EventProcessor sut = EventProcessor.getInstance();
    sut.initialize();
    MenuChoiceEvent event = new MenuChoiceEvent(this, MenuChoice.RESET_PAYMENTS);
    sut.processEvent(event);
    // verify logo page is activated
    verify(pc).onApplicationEvent(event);
    // verify command is called
    verify(cmd).executeCommand(event);
   }
  
  private void prepare() {
    pc = MockProvider.mock(PageController.class);
    cmd = MockProvider.mock(ResetPaymentCommand.class);
  };


}
