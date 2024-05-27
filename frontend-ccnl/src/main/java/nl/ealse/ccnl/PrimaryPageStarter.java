package nl.ealse.ccnl;

import java.awt.SplashScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.javafx.FXMLLoaderUtil;
import nl.ealse.javafx.ImagesMap;

/**
 * Manage the primary scene {@code Scene} handling of this application.
 *
 * @author ealse
 *
 */
@Slf4j
public class PrimaryPageStarter {
  
  @Getter
  private static final PrimaryPageStarter instance = new PrimaryPageStarter();

  private static final String MAIN_FXML = "main";

  private String applicationTitle = ApplicationProperties.getProperty("fxml.title");

  private String applicationIcon = ApplicationProperties.getProperty("fxml.icon");

  /**
   * initialize the primary scene of the application.
   */
  @EventListener
  public void onApplicationEvent(StageReadyEvent event) {
    try {
      final Stage stage = event.getStage();
      stage.setTitle(applicationTitle);
      stage.getIcons().add(ImagesMap.get(applicationIcon));
      stage.setResizable(false);
      MainStage.setStage(stage);

      Scene scene = new Scene(FXMLLoaderUtil.getPage(MAIN_FXML));
      stage.setScene(scene);
      stage.centerOnScreen();
      stage.show();
      checkScreenSize(stage);
    } catch (Exception e) {
      log.error("Start error", e);
    } finally {
      // Make sure the splash gets closed at all times
      SplashScreen splash = SplashScreen.getSplashScreen();
      if (splash != null) {
        splash.close();
      }
    }
  }

  /**
   * Adjust the size when it doesn't fit on the screen.
   *
   * @param stage - primary stage
   */
  private void checkScreenSize(final Stage stage) {
    double x = stage.getY();
    if (x < 0) {
      stage.setX(0d);
      stage.setWidth(stage.getWidth() + x);
    }
    double y = stage.getY();
    if (y < 0) {
      stage.setY(0d);
      stage.setHeight(stage.getHeight() + y);
    }
  }

}
