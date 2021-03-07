package nl.ealse.ccnl.validation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddressNumberValidator extends AbstractValidator {

  private final TextField theField;

  public AddressNumberValidator(TextField theField, Label errorMessageLabel) {
    super(errorMessageLabel, false);
    this.theField = theField;
    this.errorMessageLabel.setText("Invullen a.u.b.");
  }

  @Override
  public void validate() {
    String number = theField.getText();
    boolean result = number != null && !number.trim().isEmpty();
    if (!result) {
      this.errorMessageLabel.setText("Invullen a.u.b.");
    } else {
      result = isNumber(number);
      if (!result) {
        this.errorMessageLabel.setText("Onjuist nummer");
      }
    }
    errorMessageLabel.setVisible(!result);
    setValid(result);
  }

  private boolean isNumber(String number) {
    try {
      Integer.parseInt(number);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
