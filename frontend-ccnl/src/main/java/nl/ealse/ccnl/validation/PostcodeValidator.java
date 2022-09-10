package nl.ealse.ccnl.validation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PostcodeValidator extends AbstractValidator {

  private static final String DUTCH_POSTALCODE_PATTERN = "^[1-9]\\d{3}\\s*[A-Z]{2}$";

  private final TextField countryField;

  private final TextField postalCodeField;

  public PostcodeValidator(TextField countryField, TextField postalCodeField,
      Label errorMessageLabel) {
    super(errorMessageLabel, false);
    this.countryField = countryField;
    this.postalCodeField = postalCodeField;
  }

  @Override
  public void validate() {
    String country = countryField.getText();
    String postalCode = postalCodeField.getText();
    boolean result = true;
    if (country == null || country.isEmpty()) {
      if (postalCode == null || postalCode.isEmpty()) {
        errorMessageLabel.setText("Invullen a.u.b.");
        result = false;
      } else {
        postalCode = postalCode.toUpperCase();
        if (!postalCode.matches(DUTCH_POSTALCODE_PATTERN)) {
          errorMessageLabel.setText("Ongeldige postcode");
          result = false;
        } else {
          postalCodeField.setText(postalCode);
        }
      }
    }
    errorMessageLabel.setVisible(!result);
    setValid(result);
  }

}
