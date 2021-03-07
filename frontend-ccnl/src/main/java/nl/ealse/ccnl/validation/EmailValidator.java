package nl.ealse.ccnl.validation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EmailValidator extends AbstractValidator {

  private final org.apache.commons.validator.routines.EmailValidator validator;

  private final TextField emailField;

  public EmailValidator(TextField emailField, Label errorMessageLabel) {
    super(errorMessageLabel, true);
    this.emailField = emailField;
    this.validator = org.apache.commons.validator.routines.EmailValidator.getInstance();
  }

  @Override
  public void validate() {

    String email = emailField.getText();
    if (email != null && !email.isEmpty()) {
      boolean result = validator.isValid(email);
      errorMessageLabel.setText("Email incorrect");
      errorMessageLabel.setVisible(!result);
      setValid(result);
    } else {
      // return true when EMAIL address is not set.
      errorMessageLabel.setVisible(false);
      setValid(true);
    }
  }

}
