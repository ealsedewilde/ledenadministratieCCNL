package nl.ealse.ccnl.test;

import static org.hamcrest.Matchers.equalTo;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import nl.ealse.ccnl.MainStage;
import nl.ealse.javafx.ImagesMap;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.awaitility.Awaitility;

public abstract class FXBase {

  static {
    new JFXPanel();
    Platform.setImplicitExit(false);
    initializeMainStage();
  }

  protected AtomicBoolean runFX(Runnable runnable, final AtomicBoolean ar) {
    Platform.runLater(runnable);
    Awaitility.await().atMost(Duration.ofSeconds(6000)).untilAtomic(ar, equalTo(Boolean.TRUE));
    return ar;
  }

  private static void initializeMainStage() {
    Field icon = FieldUtils.getField(MainStage.class, "icon", true);
    try {
      icon.set(null, ImagesMap.get("Citroen.png"));
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
