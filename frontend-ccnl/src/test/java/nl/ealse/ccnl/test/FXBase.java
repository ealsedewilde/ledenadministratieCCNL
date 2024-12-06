package nl.ealse.ccnl.test;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.MainStage;
import nl.ealse.javafx.ImagesMap;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * All unit tests that need to run in the JavaFx application thread must extend this class
 */
@Slf4j
public abstract class FXBase {

  /**
   * To enhance performance, all JavaFx test should run in the same tookit
   */
  static {
    Platform.startup(() -> {
    });
    Platform.setImplicitExit(false);
    initializeMainStage();
  }

  /**
   * Controlled run of some work in the JavaFx application thread.
   * @param work
   * @return true when successful
   */
  protected boolean runFX(Runnable work) {
    FutureTask<Boolean> task = new FutureTask<>(() -> {
      work.run();
      return true;
    });
    Platform.runLater(task);
    try {
      // Safeguard for hanging tests. Such tests time out after 6 seconds.
      return task.get(6, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      log.error("Exception in Runnable", e);
      return false;
    }
  }

  private static void initializeMainStage() {
    Field icon = FieldUtils.getField(MainStage.class, "icon", true);
    try {
      icon.set(null, ImagesMap.get("Citroen.png"));
    } catch (IllegalArgumentException | IllegalAccessException e) {
      log.error("Could not initialize MainStage icon", e);
      throw new ExceptionInInitializerError();
    }
  }
}
