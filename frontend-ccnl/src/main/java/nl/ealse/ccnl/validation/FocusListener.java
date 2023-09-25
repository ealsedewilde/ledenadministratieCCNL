package nl.ealse.ccnl.validation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * This listener should avoid an error message as soon as a form page opens.
 * <p>
 * When a new form page opens, the first field of that page get focus.
 * In this situation validation of the field content is suspended until it
 * looses focus.
 * </p>
 */
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
