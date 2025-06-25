package nl.ealse.ccnl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;

public class TaskExecutor {

  /**
   * Execute a task; end with optional closing EntityManager.
   *
   * @param task to execute asynchronously
   */
  public void execute(Runnable task) {
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.execute(() -> {
      task.run();
      ApplicationContext.getEntityManagerProvider().cleanup();
    });
  }


}
