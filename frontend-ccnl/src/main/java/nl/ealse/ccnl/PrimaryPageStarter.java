package nl.ealse.ccnl;

import java.awt.SplashScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Manage the primary scene {@code Scene} handling of this application.
 * 
 * @author ealse
 *
 */
@Component
@Slf4j
public class PrimaryPageStarter {

  private static final String MAIN_FXML = "main";

  @Value("${fxml.title}")
  private String applicationTitle;

  @Value("${fxml.icon}")
  private String applicationIcon;

  /**
   * initialize the primary scene of the application.
   */
  @EventListener
  public void onApplicationEvent(StageReadyEvent event) {
    try {
      final Stage stage = event.getStage();
      stage.setTitle(applicationTitle);
      stage.getIcons().add(ImagesMap.get(applicationIcon));
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
