package nl.ealse.ccnl.form;

import javafx.scene.layout.GridPane;
import nl.ealse.javafx.FXMLLoaderBean;

public class FormPane extends GridPane {
  
  public FormPane(String fxmlName, Object controller) {
    FXMLLoaderBean.getPage(fxmlName, this, controller);
  }

}
