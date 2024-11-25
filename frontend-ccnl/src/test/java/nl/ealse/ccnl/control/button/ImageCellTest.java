package nl.ealse.ccnl.control.button;

import static org.mockito.Mockito.mock;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageCellTest extends FXBase {

  private ImageCell<Object> sut;

  @Test
  void performTests() {
    Assertions.assertTrue(runFX(() -> {
      testCell();
    }));

  }

  @SuppressWarnings("unchecked")
  private void testCell() {
    EventHandler<MouseEvent> eventHandler = mock(EventHandler.class);
    sut = new ImageCell<>("add.png", eventHandler);
    sut.updateItem(null, true);
    Assertions.assertNull(sut.getGraphic());
    sut.updateItem(null, false);
    Assertions.assertNotNull(sut.getGraphic());
    Platform.setImplicitExit(false);
  }

}
