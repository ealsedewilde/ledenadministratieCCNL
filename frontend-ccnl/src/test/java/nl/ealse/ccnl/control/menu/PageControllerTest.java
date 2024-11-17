package nl.ealse.ccnl.control.menu;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.FXMLLoaderUtil;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageControllerTest extends FXBase {
  private static final String MAIN_FXML = "main";

  private PageController sut;


  @Test
  void performTests() {

    Assertions.assertTrue(runFX(() -> {
      testController();
      return Boolean.TRUE;
    }));
    
  }

  private void testController() {
    ScrollPane m = (ScrollPane) FXMLLoaderUtil.getPage(MAIN_FXML);
    VBox content = (VBox) m.getContent();
    BorderPane p = (BorderPane) content.getChildren().get(1);
    Node c = p.getCenter();
    Assertions.assertTrue(c instanceof VBox);
    setBorderPane(p);
    sut.activateLogoPage();
    sut.showErrorMessage("error");
    sut.showMessage("message");
    sut.showPermanentMessage("permanent");
  }

  @BeforeEach
  void setup() {
    sut = new PageController();
  }

  private void setBorderPane(BorderPane pane) {
    try {
      FieldUtils.writeField(sut, "mainPage", pane, true);
      FieldUtils.writeField(sut, "mainInfo", new Label(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
