package nl.ealse.ccnl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.event.support.EventProcessor;

@UtilityClass
class StartContext {
  
  @Getter
  private final ExecutorService threadPool = Executors.newFixedThreadPool(3);
  
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
   * Optimistic scenario; assume we can start the application..
   * Perform checking tasks asynchronously and check the conditions later on.
   */
  public void start() {
    unique = threadPool.submit(new UniqueCheck());
    initialized = threadPool.submit(() -> EventProcessor.getInstance().initialize(), Boolean.TRUE);
  }

}
