package nl.ealse.ccnl.database.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Disabled
class DbConfiguratorTest extends FXMLBaseTest {

  private static ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);
  private static ApplicationContext context = mock(ApplicationContext.class);
  private static PageController pageController = mock(PageController.class);
  private DbConfigurator sut;

  @Test
  void testViewer() {
    AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    sut.openDialog();
    assertTrue(sut.getStage().isShowing());
    sut.getStage().close();
    sut.configureExistingDatabase();
    assertTrue(sut.getStage().isShowing());
    sut.getStage().close();
    sut.configureNewDatabase();
    assertTrue(sut.getStage().isShowing());
    sut.save();
    String msg = sut.getMessage().getText();
    assertEquals("Geef een database locatie op Geef een databasenaam op", msg);
  }

  private void prepare() {
    sut = spy(new DbConfigurator(new Stage(), stage -> {
    }));
    doNothing().when(sut).nextAction();
  }

  @BeforeAll
  static void setup() {
    when(context.getEnvironment()).thenReturn(environment);
    when(context.getBean("pageController")).thenReturn(pageController);
  }
}
