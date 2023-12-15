package nl.ealse.ccnl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;

public class TaskExecutor {
  
  private static final ExecutorService service = Executors.newFixedThreadPool(10);
  
  @Getter
  static TaskExecutor instance = new TaskExecutor();
  
  public void execute(Runnable task) {
    service.execute(task);
  }
  
  

}
