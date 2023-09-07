package nl.ealse.ccnl.control;

import javafx.concurrent.Task;
import nl.ealse.ccnl.control.menu.PageController;

/**
 * A asynchronous task task sends a result message to be shown.
 */
public abstract class HandledTask extends Task<String>{

  private final PageController pageController;

  public HandledTask(PageController pageController) {
    super();
    this.pageController = pageController;
    this.setOnSucceeded(
        (evt) -> pageController.showMessage(evt.getSource().getValue().toString()));
    this.setOnFailed(
        (evt) -> {
          Object t =  evt.getSource().getException();
          pageController.showErrorMessage(evt.getSource().getException().getMessage());
        });
  }

}
