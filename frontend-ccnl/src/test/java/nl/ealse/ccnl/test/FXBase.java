package nl.ealse.ccnl.test;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.MainStage;
import nl.ealse.javafx.ImagesMap;
import org.apache.commons.lang3.reflect.FieldUtils;

@Slf4j
public abstract class FXBase {

  static {
    Platform.startup(() -> {});
    Platform.setImplicitExit(false);
    initializeMainStage();
  }

  protected boolean runFX(Callable<Boolean> work) {
    FutureTask<Boolean> task = new FutureTask<>(work);
    Platform.runLater(task);
    try {
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
