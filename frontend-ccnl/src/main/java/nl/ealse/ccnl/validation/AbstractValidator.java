package nl.ealse.ccnl.validation;

import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractValidator implements ContentValidator {

  protected final Label errorMessageLabel;
  private final boolean defaultValid;

  @Setter
  private CallbackLauncher callbackLauncher;

  @Getter
  private boolean valid;

  protected AbstractValidator(Label errorMessageLabel, boolean defaultValid) {
    this.errorMessageLabel = errorMessageLabel;
    this.defaultValid = defaultValid;
    this.valid = defaultValid;
  }

  protected void setValid(boolean valid) {
    this.valid = valid;
    callbackLauncher.fireCallback();
  }

  /**
   * Called when JavaFX control gains focus (The control is bound to be changed by the user.)
   */
  public void reset() {
    errorMessageLabel.setVisible(false);
  }

  public void initialize() {
    valid = defaultValid;
    errorMessageLabel.setVisible(false);
  }

}
