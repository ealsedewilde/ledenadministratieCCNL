package nl.ealse.ccnl.control.menu;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.PageId;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.junit.jupiter.api.Assertions;
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
      springContext = mock(ApplicationContext.class);
      fxmlNodeMap = new FXMLNodeMap(springContext);
      testController();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void testController() {
    sut = new PageController(fxmlNodeMap);
    when(springContext.getBean(PageController.class)).thenReturn(sut);
    MenuController mc = new MenuController(springContext, sut);
    when(springContext.getBean(MenuController.class)).thenReturn(mc);
    try {
      fxmlNodeMap.get(MAIN_FXML);
      Parent p = sut.loadPage(PageName.LOGO);
      Assertions.assertTrue(p instanceof VBox);
      sut.setActivePage(PageName.LOGO);
      sut.showErrorMessage("error");
      sut.showMessage("message");
      StageReadyEvent event = mock(StageReadyEvent.class);
      sut.onApplicationEvent(event);
      Platform.setImplicitExit(false);
    } catch (FXMLMissingException e) {
      e.printStackTrace();
    }
  }

}
