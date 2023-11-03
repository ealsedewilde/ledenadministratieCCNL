package nl.ealse.ccnl;

<<<<<<< HEAD
import javafx.stage.Stage;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MainStage {
  
  @Getter
  Stage stage;
  
  void setStage(Stage s) {
    stage = s;
=======
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * Utility with a reference to the primary Stage. It is used to set the initOwner in various sub
 * Stages.
 */
@UtilityClass
public class MainStage {

  @Getter
  private Stage stage;
  
  @Getter
  private Image icon;
  

  void setStage(Stage s) {
    stage = s;
    icon = s.getIcons().get(0);
>>>>>>> branch 'main' of git@github.com:ealsedewilde/ledenadministratieCCNL.git
  }
}
