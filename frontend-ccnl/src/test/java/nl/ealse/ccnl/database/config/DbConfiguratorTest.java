package nl.ealse.ccnl.database.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.ledenadministratie.config.DatabasePropertySource;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

class DbConfiguratorTest extends FXMLBaseTest {
  
  private static ConfigurableEnvironment environment = new TestEnvironment();
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
    //sut.save();
  }
  
  private void prepare() {
    sut = new DbConfigurator(new Stage(), stage -> {});
  }
  
  @BeforeAll
  static void setup() {
    when(context.getEnvironment()).thenReturn(environment);
    when(context.getBean("pageController")).thenReturn(pageController);
  }

  private static class TestEnvironment extends MockEnvironment {
    
    private DatabasePropertySource databaseSource;
    
    TestEnvironment() {
      this.databaseSource = new DatabasePropertySource(this);
      getPropertySources().addFirst(databaseSource);
    }
    
  }
}
