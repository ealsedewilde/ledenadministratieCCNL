package nl.ealse.ccnl.control.menu;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.FXMLLoaderBean;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class PageControllerTest extends FXBase {
  private static final String MAIN_FXML = "main";

  private static ApplicationContext springContext;

  private PageController sut;


  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      testController();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void testController() {
    ScrollPane m = (ScrollPane) FXMLLoaderBean.getPage(MAIN_FXML);
    VBox content = (VBox) m.getContent();
    BorderPane p = (BorderPane) content.getChildren().get(1);
    Node c = p.getCenter();
    Assertions.assertTrue(c instanceof VBox);
    setBorderPane(p);
    sut.activateLogoPage();
    sut.showErrorMessage("error");
    sut.showMessage("message");
    sut.showPermanentMessage("permanent");
    StageReadyEvent event = mock(StageReadyEvent.class);
    sut.onApplicationEvent(event);
  }

  @BeforeEach
  void setup() {
    springContext = mock(ApplicationContext.class);
    setFxmlDirectory(new FXMLLoaderBean(springContext));
    sut = new PageController();
    when(springContext.getBean(PageController.class)).thenReturn(sut);
    MenuController mc = new MenuController(springContext);
    when(springContext.getBean(MenuController.class)).thenReturn(mc);
  }

  private void setBorderPane(BorderPane pane) {
    try {
      FieldUtils.writeField(sut, "mainPage", pane, true);
      FieldUtils.writeField(sut, "mainInfo", new Label(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setFxmlDirectory(FXMLLoaderBean flb) {
    try {
      FieldUtils.writeField(flb, "fxmlDirectory", "fxml/", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
