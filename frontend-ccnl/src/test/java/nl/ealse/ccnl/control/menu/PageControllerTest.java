package nl.ealse.ccnl.control.menu;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.PageId;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class PageControllerTest extends FXBase {
  private static final PageId MAIN_FXML = new PageId("main", "main");

  private static ApplicationContext springContext;
  private static FXMLNodeMap fxmlNodeMap;

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
    try {
      ScrollPane m = (ScrollPane) fxmlNodeMap.get(MAIN_FXML, null);
      VBox content = (VBox) m.getContent();
      BorderPane p = (BorderPane) content.getChildren().get(1);
      Node c = p.getCenter();
      Assertions.assertTrue(c instanceof VBox);
      sut.activateLogoPage();
      sut.showErrorMessage("error");
      sut.showMessage("message");
      sut.showPermanentMessage("permanent");
      StageReadyEvent event = mock(StageReadyEvent.class);
      sut.onApplicationEvent(event);
    } catch (FXMLMissingException e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  void setup() {
    springContext = mock(ApplicationContext.class);
    fxmlNodeMap = new FXMLNodeMap(springContext);
    sut = new PageController(fxmlNodeMap);
    setFxmlDirectory();
    when(springContext.getBean(PageController.class)).thenReturn(sut);
    MenuController mc = new MenuController(springContext);
    when(springContext.getBean(MenuController.class)).thenReturn(mc);
  }
  
  private void setFxmlDirectory() {
    try {
      Field f = fxmlNodeMap.getClass().getDeclaredField("fxmlDirectory");
      f.setAccessible(true);
      f.set(fxmlNodeMap, "fxml/");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


}
