package nl.ealse.ccnl;

import java.util.EventObject;
import javafx.stage.Stage;

/**
 * The event indicating that the application is ready to handle the JavaFX stage.
 *
 * @author ealse
 */
@SuppressWarnings("serial")
public class StageReadyEvent extends EventObject {

  public StageReadyEvent(Stage source) {
    super(source);
  }

  public Stage getStage() {
    return (Stage) getSource();
  }

}
