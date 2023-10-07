package nl.ealse.ccnl.form;

import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;

@Slf4j
public class FormPane extends GridPane {
  
  public FormPane(String fxmlName, Object controller) {
    try {
      FXMLNodeMap.getPage(fxmlName, this, controller);
    } catch (FXMLMissingException e) {
      log.error("unable to load page", e);
    }
  }

}
