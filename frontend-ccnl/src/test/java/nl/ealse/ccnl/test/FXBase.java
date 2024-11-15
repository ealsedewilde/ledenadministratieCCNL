package nl.ealse.ccnl.test;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.MainStage;
import nl.ealse.javafx.ImagesMap;
import org.apache.commons.lang3.reflect.FieldUtils;

@Slf4j
public abstract class FXBase {

  static {
    new JFXPanel();
    Platform.setImplicitExit(false);
    initializeMainStage();
  }

  protected boolean runFX(FutureTask<AtomicBoolean> task) {
    Platform.runLater(task);
    try {
      return task.get(6, TimeUnit.SECONDS).get();
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
