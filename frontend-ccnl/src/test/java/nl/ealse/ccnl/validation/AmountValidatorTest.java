package nl.ealse.ccnl.validation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AmountValidatorTest extends FXBase {

  private static TextField amountField;
  private static Label errorMessageLabel;

  private AmountValidator sut;

  @Test
  void performTests() {
    Assertions.assertTrue(runFX(() -> {
      testSut();
    }));

  }

  private void testSut() {
    sut = new AmountValidator(amountField, errorMessageLabel);
    sut.setCallbackLauncher(() -> {
    });
    sut.initialize();

    amountField.setText("30a");
    sut.validate();
    Assertions.assertTrue(errorMessageLabel.isVisible());
    sut.reset();

    amountField.setText("30");
    sut.validate();
    Assertions.assertFalse(errorMessageLabel.isVisible());
    sut.reset();
    
    amountField.setText(" 2.50");
    sut.validate();
    Assertions.assertFalse(errorMessageLabel.isVisible());
    sut.reset();
    
    amountField.setText("2,50");
    sut.validate();
    Assertions.assertFalse(errorMessageLabel.isVisible());
  
    sut.reset();
  }


  @BeforeAll
  static void setup() {
    amountField = new TextField();
    errorMessageLabel = new Label();
  }



}
