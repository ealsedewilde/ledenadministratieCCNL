package nl.ealse.ccnl.validation;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest extends FXBase {

  private static TextField emailField;
  private static Label errorMessageLabel;

  private EmailValidator sut;

  @Test
  void performTests() {

    Assertions.assertTrue(runFX(() -> {
      testSut();
      return Boolean.TRUE;
    }));
    
  }

  private void testSut() {
    sut = new EmailValidator(emailField, errorMessageLabel);
    sut.setCallbackLauncher(() -> {
    });
    sut.initialize();

    sut.validate();
    Assertions.assertFalse(errorMessageLabel.isVisible());

    emailField.setText("foo");
    sut.validate();
    Assertions.assertTrue(errorMessageLabel.isVisible());

    sut.reset();
    Platform.setImplicitExit(false);
  }

  @BeforeAll
  static void setup() {
    emailField = new TextField();
    errorMessageLabel = new Label();
  }

}
