package nl.ealse.ccnl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.MenuController;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.javafx.FXMLNodeMap;
import nl.ealse.javafx.SpringJavaFXBase.StageReadyEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class PrimaryPageStarterTest extends FXBase {

  private static ApplicationContext context;
  private static MenuController menuController;
  private static PageController pageController;
  private static TestNodeMap fxmlNodeMap;

  private PrimaryPageStarter sut;

  @Test
  void testSut() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      fxmlNodeMap = new TestNodeMap(context);
      sut = new PrimaryPageStarter(fxmlNodeMap);
      config();
      
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

  }

  private void doTest() {
    Stage s = new Stage();
    StageReadyEvent event = new StageReadyEvent(s);
    sut.onApplicationEvent(event);
    Assertions.assertTrue(s.isShowing());
  }

  @BeforeAll
  static void setup() {
    context = mock(ApplicationContext.class);
    menuController = mock(MenuController.class);
    when(context.getBean(MenuController.class)).thenReturn(menuController);
    pageController = mock(PageController.class);
    when(context.getBean(PageController.class)).thenReturn(pageController);
  }

  private static class TestNodeMap extends FXMLNodeMap {

    public TestNodeMap(ApplicationContext springContext) {
      super(springContext);
    }
  }

  private void config() {
    try {
      FieldUtils.writeDeclaredField(sut, "applicationIcon", "Citroen.png", true);
      FieldUtils.writeField(fxmlNodeMap, "fxmlDirectory", "fxml/", true);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
