package nl.ealse.ccnl.control;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.ImagesMap;

/**
 * Build info pages.
 */
public class StageBuilder {
  
  private final Stage stage = new Stage();
  private double width;
  private double height;
  private Parent parent;
  
  public StageBuilder(PageController pageController) {
    stage.initOwner(pageController.getPrimaryStage());
  }
  
  public StageBuilder fxml(String fxmlName, Object controller) {
    parent = FXMLLoaderBean.getPage(fxmlName, controller);
    return this;
  }

  public StageBuilder title(String title) {
    stage.setTitle(title);
    return this;
  }
  
  public StageBuilder size(double width, double height) {
    this.width = width;
    this.height = height;
    return this;
  }
  
  public Stage build() {
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);
    stage.getIcons().add(ImagesMap.get("info.png"));
    Scene scene = new Scene(parent, width, height);
    stage.setScene(scene);
    return stage;
  }

}
