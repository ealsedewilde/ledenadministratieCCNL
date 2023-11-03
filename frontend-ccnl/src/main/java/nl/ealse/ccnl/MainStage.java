package nl.ealse.ccnl;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MainStage {
  
  @Getter
  Stage stage;
  
  void setStage(Stage s) {
    stage = s;
  }
}
