package nl.ealse.ccnl.control;

import javafx.concurrent.Task;
import nl.ealse.ccnl.TaskExecutor;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;

/**
 * A asynchronous task task sends a result message to be shown.
 */
public abstract class HandledTask extends Task<String> {

  protected HandledTask() {
    super();
    initialize();
  }

  private void initialize() {
    PageController pageController = ApplicationContext.getComponent(PageController.class);
    this.setOnSucceeded(evt -> pageController.showMessage(evt.getSource().getValue().toString()));
    this.setOnFailed(evt -> {
      Throwable t = evt.getSource().getException();
      pageController.showErrorMessage(t.getMessage());
    });
  }

  public void executeTask() {
    TaskExecutor executor = ApplicationContext.getComponent(TaskExecutor.class);
    executor.execute(this);
  }

}
