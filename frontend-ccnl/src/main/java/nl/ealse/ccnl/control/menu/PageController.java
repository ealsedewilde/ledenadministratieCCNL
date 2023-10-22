package nl.ealse.ccnl.control.menu;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

/**
 * Controller of the main page of the application.
 * <p>
 * The main page is a GridPane. The content of Center of this pane is replaced frequently as part of
 * the user dialog. This controller is also capable of displaying messages in the Bottom part of the
 * GridPane.
 * </p>
 * <p>
 * For convenience it provides a reference to the primary stage of this application.
 * </p>
 *
 * @author ealse
 */
@Controller
public class PageController {

  @FXML
  private BorderPane mainPage;

  @FXML
  private Label mainInfo;

  @FXML
  private ImageView logo;

  @Getter
  private Stage primaryStage;

  private Node logoPage;

  private PauseTransition delay;

  @FXML
  void initialize() {
    logo.setImage(ImagesMap.get("CCNLLogo.png"));
    logoPage = mainPage.getCenter();
  }

  /**
   * Show the logo page.
   */
  public void activateLogoPage() {
    mainPage.setCenter(logoPage);
  }

  /**
   * Load page and show it in the GUI.
   *
   * @param pageRefrence - reference to the loade fxml page
   */
  public void setActivePage(PageReference pageReference) {
    mainPage.setCenter(pageReference.getPage());
  }

  /**
   * Show a message.
   * <p>
   * Use this when starting a async Task. Replace this message when the async Task is finshed.
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
   * When menu choice process with a fxml page is fully executed, the logo page will be shown.
   * However when this is not the case when the process is aborted. In that case the last fxml page
   * keeps showing. Ìf you then choose a menu choice process without a fxml page, the fxml page must
   * be replaced by the logo. This must be done before handling the command, hence the @Order.
   * </p>
   */
  @EventListener(condition = "#event.command()")
  @Order(10)
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
