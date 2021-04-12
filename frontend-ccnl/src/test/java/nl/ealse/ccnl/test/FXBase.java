package nl.ealse.ccnl.test;

import static org.hamcrest.Matchers.equalTo;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.awaitility.Awaitility;

public abstract class FXBase {

  static {
    new JFXPanel();
    Platform.setImplicitExit(false);
  }

  protected AtomicBoolean runFX(Runnable runnable, final AtomicBoolean ar) {
    Platform.runLater(runnable);
    Awaitility.await().atMost(Duration.ofSeconds(6000)).untilAtomic(ar, equalTo(Boolean.TRUE));
    return ar;
  }
}
