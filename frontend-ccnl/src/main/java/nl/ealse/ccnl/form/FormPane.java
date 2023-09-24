package nl.ealse.ccnl.form;

import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.PageId;

@Slf4j
public class FormPane extends GridPane {
  
  public FormPane(PageId pageId, Object controller) {
    try {
      FXMLNodeMap.getPage(pageId, this, controller);
    } catch (FXMLMissingException e) {
      log.error("unable to load page", e);
    }
  }

}
