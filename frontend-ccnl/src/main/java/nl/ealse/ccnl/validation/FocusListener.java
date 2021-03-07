package nl.ealse.ccnl.validation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FocusListener implements ChangeListener<Boolean> {

  private final ContentValidator contentValidator;

  public FocusListener(ContentValidator contentValidator) {
    this.contentValidator = contentValidator;
  }

  @Override
  public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
      Boolean newValue) {
    if (newValue != null && newValue.booleanValue()) {
      // JavaFX control gained focus
      contentValidator.reset();
    } else {
      // JavaFX control lost focus
      contentValidator.validate();
    }

  }

}
