package nl.ealse.ccnl;

import java.awt.SplashScreen;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.PageId;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Manage the primary scene {@code Scene} handling of this application.
 * 
 * @author ealse
 *
 */
@Component
@Slf4j
public class PrimaryPageStarter implements ApplicationListener<StageReadyEvent> {

  private static final PageId MAIN_FXML = new PageId("main", "main");

  private final FXMLNodeMap fxmlNodeMap;

  @Value("${fxml.title}")
  private String applicationTitle;

  @Value("${fxml.icon}")
  private String applicationIcon;

  public PrimaryPageStarter(FXMLNodeMap fxmlNodeMap) {
    this.fxmlNodeMap = fxmlNodeMap;
  }

  /**
   * initialize the primary scene of the application.
   */
  @Override
  public void onApplicationEvent(StageReadyEvent event) {
    try {
      final Stage stage = event.getStage();
      stage.setTitle(applicationTitle);
      stage.getIcons().add(ImagesMap.get(applicationIcon));
      Scene scene = new Scene(getRoot());
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

  private Parent getRoot() {
    Parent root;
    try {
      root = fxmlNodeMap.get(MAIN_FXML);
    } catch (FXMLMissingException e) {
      root = new Label(e.getMessage() + e.getPagekey());
      root.setStyle("-fx-text-fill: red; -fx-font-size: 30; -fx-font-weight: bold;");
    }
    return root;
  }

}
