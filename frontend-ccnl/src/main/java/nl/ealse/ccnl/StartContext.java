package nl.ealse.ccnl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.event.support.EventPublisher;

@UtilityClass
class StartContext {

  @Getter
  private final ExecutorService threadPool = Executors.newFixedThreadPool(3, r -> {
    Thread t = Executors.defaultThreadFactory().newThread(r);
    t.setDaemon(true);
    return t;
  });

  /**
   * Indication whether this application instance is the only one running.
   */
  @Getter
  private Future<Boolean> unique;

  /**
   * Indication whether the eventing system started successfully.
   */
  @Getter
  private Future<Boolean> initialized;

  /**
   * Optimistic scenario; assume we can start the application. 
   * Perform checking task asynchronously and check the condition later on.
   */
  public void performUniqueCheck() {
    unique = threadPool.submit(new UniqueCheck());
  }
  
  public void loadEventRegistry() {
    initialized = threadPool.submit(new EventPublisher.EventRegistryLoader());
  }

}
