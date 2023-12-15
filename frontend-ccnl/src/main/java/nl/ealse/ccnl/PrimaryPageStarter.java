package nl.ealse.ccnl;

import java.awt.SplashScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.javafx.FXMLLoaderBean;
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

      Scene scene = new Scene(FXMLLoaderBean.getPage(MAIN_FXML));
      stage.setScene(scene);
      stage.show();
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

}
