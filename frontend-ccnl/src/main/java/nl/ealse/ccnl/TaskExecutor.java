package nl.ealse.ccnl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

public class TaskExecutor {

  private static final ExecutorService service = Executors.newSingleThreadExecutor();

  /**
   * Execute a task; end with optional closing EntityManager.
   *
   * @param task to execute asynchronously
   */
  public void execute(Runnable task) {
    service.execute(() -> {
      task.run();
      EntityManagerProvider.cleanup();
    });
  }


}
