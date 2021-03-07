package nl.ealse.ccnl.validation;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import org.apache.commons.validator.routines.IBANValidator;

public class IbanNumberValidator extends AbstractValidator {

  private static final IBANValidator ibanValidator = new IBANValidator();

  private final ChoiceBox<String> paymentMethod;

  private final TextField ibanNumberField;

  public IbanNumberValidator(ChoiceBox<String> paymentMethod, TextField ibanNumber,
      Label errorMessageLabel) {
    super(errorMessageLabel, true);
    this.paymentMethod = paymentMethod;
    this.ibanNumberField = ibanNumber;
  }

  @Override
  public void validate() {
    boolean result = true;
    String ibanNumber = ibanNumberField.getText();
    if (ibanNumber == null || ibanNumber.isEmpty()) {
      if (PaymentMethodMapper.DIRECT_DEBIT
          .equals(paymentMethod.getSelectionModel().getSelectedItem())) {
        errorMessageLabel.setText("Invullen a.u.b.");
        result = false;
      }
    } else {
      ibanNumber = ibanNumber.toUpperCase();
      ibanNumberField.setText(ibanNumber);
      result = ibanValidator.isValid(ibanNumber);
      errorMessageLabel.setText("IBAN-nummer onjuist");
    }
    errorMessageLabel.setVisible(!result);
    setValid(result);
  }

}
