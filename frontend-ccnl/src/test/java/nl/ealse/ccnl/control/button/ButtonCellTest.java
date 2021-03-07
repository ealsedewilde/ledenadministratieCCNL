package nl.ealse.ccnl.control.button;

import static org.mockito.Mockito.mock;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ButtonCellTest extends FXBase {

  private ButtonCell<AddButton, Object> sut;

  
  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      testCell();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  @SuppressWarnings("unchecked")
  private void testCell() {
    EventHandler<ActionEvent> eventHandler = mock(EventHandler.class);
    sut = new ButtonCell<>(new AddButton(), eventHandler);
    sut.updateItem(null, true);
    Assertions.assertNull(sut.getGraphic());
    sut.updateItem(null, false);
    Assertions.assertNotNull(sut.getGraphic());
    Platform.setImplicitExit(false);
  }

}
