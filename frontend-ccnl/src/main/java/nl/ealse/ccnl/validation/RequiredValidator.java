package nl.ealse.ccnl.validation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RequiredValidator extends AbstractValidator implements ChangeListener<String> {

  private final TextField theField;

  /**
   * Construct a validator.
   * 
   * @param theField - field to validate
   * @param errorMessageLabel - label for error message when invalid
   */
  public RequiredValidator(TextField theField, Label errorMessageLabel) {
    super(errorMessageLabel, false);
    this.theField = theField;
    this.theField.textProperty().addListener(this);
    this.errorMessageLabel.setText("Invullen a.u.b.");
  }

  @Override
  public void validate() {
    boolean result = theField.getText() != null && !theField.getText().trim().isEmpty();
    errorMessageLabel.setVisible(!result);
    setValid(result);
  }

  @Override
  public void changed(ObservableValue<? extends String> observable, String oldValue,
      String newValue) {
    validate();
  }

}
