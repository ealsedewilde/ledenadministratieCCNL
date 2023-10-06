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
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.PageId;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.springframework.context.event.EventListener;
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
public class PageController {

  private static final PageId LOGO_ID = new PageId("LOGO", "logo");

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
  void initialize() {
    logo.setImage(ImagesMap.get("CCNLLogo.png"));
  }

  public void activateLogoPage() {
    Parent page = loadPage(LOGO_ID, null);
    mainPage.setCenter(page);
  }

  /**
   * Load fxml and show it in the GUI.
   * @param pageName
   */
  public void setActivePage(PageName pageName) {
    Parent page = loadPage(pageName.getId(), null);
    mainPage.setCenter(page);
  }

  /**
   * Load fxml without showing it in the GUI.
   * <p>
   * The fxml must NOT have a fx:controller attribute!
   * </p>
   * @param pageName reference to a fxml file
   * @param controller to bind to the fxml
   * @return the loaded fxml
   */
  public Parent loadPage(PageName pageName, Object controller) {
    // Use this method when loading a page in the @PostConstruct of a Controller
    // (At that stage, the Controller is not yet available in the Spring ApplicationContext,
    //  because a lookup via fx:controller in the fxml is not possible.)
    return loadPage(pageName.getId(), controller);
  }

  /**
   * Load fxml without showing it in the GUI.
   * <p>
   * The fxml must have a fx:controller attribute!
   * </p>
   * @param pageName reference to a fxml file
   * @return the loaded fxml
   */
  public Parent loadPage(PageName pageName) {
    return loadPage(pageName.getId(), null);
  }

  private Parent loadPage(PageId pageId, Object controller) {
    try {
      return fxmlNodeMap.get(pageId, controller);
    } catch (FXMLMissingException e) {
      Parent errorLabel = new Label(e.getMessage() + e.getPagekey());
      errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 30; -fx-font-weight: bold;");
      return errorLabel;
    }
  }

  /**
   * Show a message.
   * <p>
   * Use this when starting a async Task.
   * Replace this message when the asybc Task is finshed.
   * </p>
   */
  public void showPermanentMessage(String message) {
    if (delay != null) {
      delay.stop();
    }
    mainInfo.getStyleClass().clear();
    mainInfo.getStyleClass().add("info");
    mainInfo.setText(message);
  }

  /**
   * Show a message for a period of 5 seconds.
   */
  public void showMessage(String message) {
    mainInfo.getStyleClass().clear();
    mainInfo.getStyleClass().add("info");
    mainInfo.setText(message);
    delay = new PauseTransition(Duration.seconds(5));
    delay.setOnFinished(event -> mainInfo.setText(""));
    delay.play();
  }

  /**
   * Show an error message for a period of 5 seconds.
   */
  public void showErrorMessage(String message) {
    mainInfo.getStyleClass().clear();
    mainInfo.getStyleClass().add("error");
    mainInfo.setText(message);
    delay = new PauseTransition(Duration.seconds(5));
    delay.setOnFinished(event -> mainInfo.setText(""));
    delay.play();
  }

  /**
   * Set the logo page. 
   * <p>
   * When menu choice process with a fxml page is fully executed, the logo page
   * will be shown. However when this is not the case when the process is aborted. In that case the
   * last fxml page keeps showing. Ìf you then chooce a menu choice process without a fxml page, the
   * fxml page must be replaced by the logo.
   * </p>
   */
  @EventListener(condition = "#event.command()")
  public void onApplicationEvent(MenuChoiceEvent event) {
    activateLogoPage();
  }

  /**
   * Receive the primary stage. All JavaFX controllers will have a reference to this component. Thus
   * all JavaFX controllers can lookup the primary stage when needed for modal pages.
   */
  @EventListener
  public void onApplicationEvent(StageReadyEvent event) {
    this.primaryStage = event.getStage();
  }

}
