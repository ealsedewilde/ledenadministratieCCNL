package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResetPaymentControllerTest extends FXMLBaseTest {

  private ResetPaymentCommand sut;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      sut = getTestSubject(ResetPaymentCommand.class);
      clickButton();
      sut.executeCommand(null);
      verify(getPageController()).showMessage("Alle betaalgegevens zijn gewist");
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  /**
   * Click the confirm button. This code gets executed when the main FX thread waits for input.
   */
  private void clickButton() {
    Platform.runLater(() -> {
      Button yes = getButton();
      yes.fire();
    });
  }

  private Button getButton() {
    try {
      Alert confirmation = (Alert) FieldUtils.readField(sut, "confirmation", true);
      DialogPane pane = confirmation.getDialogPane();
      ButtonType yes = pane.getButtonTypes().get(0);
      return (Button) pane.lookupButton(yes);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

}
