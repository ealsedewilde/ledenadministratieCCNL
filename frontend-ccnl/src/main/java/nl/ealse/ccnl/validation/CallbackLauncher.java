package nl.ealse.ccnl.validation;

import java.util.function.Consumer;

/**
 * Interface for a callback to the user interface.
 * The user interface can set a {@link Consumer} to receive whether the form has valid content.
 *This is used to eanble/disable the save button of the form.
 */
public interface CallbackLauncher {

  public void fireCallback();

}
