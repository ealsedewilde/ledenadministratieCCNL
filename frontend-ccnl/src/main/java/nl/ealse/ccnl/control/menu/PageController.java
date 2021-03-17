package nl.ealse.ccnl.control.menu;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

/**
 * Controller of the main page of the application. The main page is a GridPane. The content of
 * Center of this pane is replaced frequently as part of the user dialog. This controller is also
 * capable of displaying messages in the Bottom part of the GridPane.
 * 
 * For convenience reasons this controller also has the capability to pre load fxml pages.
 * Furthermore it can provide a reference to the primary satge of this application.
 * 
 * @author ealse
 */
@Controller
public class PageController implements ApplicationListener<StageReadyEvent> {

  private final FXMLNodeMap fxmlNodeMap;

  @FXML
  private BorderPane mainPage;

  @FXML
  private Label mainInfo;

  @FXML
  private ImageView logo;

  @Getter
  private Stage primaryStage;
  
  private PauseTransition delay;

  public PageController(FXMLNodeMap fxmlNodeMap) {
    this.fxmlNodeMap = fxmlNodeMap;
  }

  @FXML
  public void initialize() {
    logo.setImage(ImagesMap.get("CCNLLogo.png"));
  }

  public Parent loadPage(PageName pageName) {
    try {
      return fxmlNodeMap.get(pageName.getId());
    } catch (FXMLMissingException e) {
      Parent errorLabel = new Label(e.getMessage() + e.getPagekey());
      errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 30; -fx-font-weight: bold;");
      return errorLabel;
    }
  }

  public void setActivePage(PageName pageName) {
    Parent page = loadPage(pageName);
    mainPage.setCenter(page);
  }

  public void showPermanentMessage(String message) {
    if (delay != null) {
      delay.stop();
    }
    mainInfo.getStyleClass().clear();
    mainInfo.getStyleClass().add("info");
    mainInfo.setText(message);
  }

  public void showMessage(String message) {
    mainInfo.getStyleClass().clear();
    mainInfo.getStyleClass().add("info");
    mainInfo.setText(message);
    delay = new PauseTransition(Duration.seconds(5));
    delay.setOnFinished(event -> mainInfo.setText(""));
    delay.play();
  }

  public void showErrorMessage(String message) {
    mainInfo.getStyleClass().clear();
    mainInfo.getStyleClass().add("error");
    mainInfo.setText(message);
    delay = new PauseTransition(Duration.seconds(5));
    delay.setOnFinished(event -> mainInfo.setText(""));
    delay.play();
  }

  /**
   * Receive the primary stage. All JavaFX controllers will have a reference to this component. Thus
   * all JavaFX controllers can lookup the primary stage when needed for modal pages.
   */
  @Override
  public void onApplicationEvent(StageReadyEvent event) {
    this.primaryStage = event.getStage();
  }

}
