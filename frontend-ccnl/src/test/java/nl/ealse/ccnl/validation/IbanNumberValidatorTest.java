package nl.ealse.ccnl.validation;

import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IbanNumberValidatorTest extends FXBase {

  private static ChoiceBox<String> paymentMethod;
  private static TextField ibanNumberField;
  private static Label errorMessageLabel;

  private IbanNumberValidator sut;

  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    runFX(new FutureTask<AtomicBoolean>(() -> {
      testSut();
      ar.set(true);
    }, ar));
    
  }

  private void testSut() {
    sut = new IbanNumberValidator(paymentMethod, ibanNumberField, errorMessageLabel);
    sut.setCallbackLauncher(() -> {
    });
    sut.initialize();

    sut.validate();
    String result = errorMessageLabel.getText();
    Assertions.assertEquals("Invullen a.u.b.", result);

    ibanNumberField.setText("nl91 abna 0417 1643 00");
    sut.validate();
    result = ibanNumberField.getText();
    Assertions.assertFalse(errorMessageLabel.isVisible());
    Assertions.assertEquals("NL91ABNA0417164300", result);

    sut.reset();
    Platform.setImplicitExit(false);
  }


  @BeforeAll
  static void setup() {
    paymentMethod = new ChoiceBox<>();
    paymentMethod.setItems(PaymentMethodMapper.getValues());
    paymentMethod.setValue(PaymentMethodMapper.DIRECT_DEBIT);
    ibanNumberField = new TextField();
    errorMessageLabel = new Label();
  }



}
