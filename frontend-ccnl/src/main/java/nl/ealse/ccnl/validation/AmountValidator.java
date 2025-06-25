package nl.ealse.ccnl.validation;

import java.math.BigDecimal;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AmountValidator extends AbstractValidator {

  private final TextField amount;

  public AmountValidator(TextField amount, Label errorMessageLabel) {
    super(errorMessageLabel, true);
    this.amount = amount;
  }

  @Override
  public void validate() {
    String inputString = amount.getText().trim();
    inputString = inputString.replace(",", ".");
    try {
      new BigDecimal(inputString);
      setValid(true);
    } catch (NumberFormatException e) {
      errorMessageLabel.setText("Bedrag niet herkend");
      errorMessageLabel.setVisible(true);
      setValid(false);
    }
  }

}
