package nl.ealse.ccnl;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * Utility with a reference to the primary Stage.
 * It is used to set the initOwner in various sub Stages.
 */
@UtilityClass
public class MainStage {

  @Getter
  Stage stage;

  void setStage(Stage s) {
    stage = s;
  }
}
