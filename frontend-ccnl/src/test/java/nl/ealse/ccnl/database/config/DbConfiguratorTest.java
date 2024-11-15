package nl.ealse.ccnl.database.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.ImagesMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DbConfiguratorTest extends FXMLBaseTest {

  private DbConfigurator sut;
  private Stage stage;

  private static String next;

  private static boolean fileSelected = true;

  @TempDir
  File tempDir;

  @Test
  void testDbConfig() {
    AtomicBoolean ar = new AtomicBoolean();
    runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar));
    
  }

  private void doTest() {
    sut.openDialog();
    assertTrue(sut.getConfigStage().isShowing());
    stage.close();

    sut.configureExistingDatabase();
    assertEquals("c:\\temp\\db", sut.getDbFolder().getText());
    assertEquals("test", sut.getDbName().getText());
    assertTrue(sut.getConfigStage().isShowing());
    stage.close();

    fileSelected = false;
    sut.configureExistingDatabase();
    String msg = sut.getMessage().getText();
    assertEquals("Geen (geldige) database geselecteerd", msg);
    assertTrue(sut.getConfigStage().isShowing());
    stage.close();

    sut.configureNewDatabase();
    assertEquals("S:\\ledenadministratie-ccnl\\db", sut.getDbFolder().getText());
    assertEquals("ccnl", sut.getDbName().getText());
    assertTrue(sut.getConfigStage().isShowing());
    stage.close();

    File dbProps = new File("db.properties");
    sut.getDbFolder().setText(tempDir.getAbsolutePath());
    if (dbProps.exists()) {
      dbProps.delete();
    }
    clickButton();
    sut.saveNew();
    msg = sut.getMessage().getText();
    assertEquals("", msg);
    assertEquals("nextAction", next);
    assertTrue(dbProps.exists());
    dbProps.delete();
  }

  private void prepare() {
    stage = new Stage();
    sut = new TestDbConfigurator(stage);
  }

  @BeforeAll
  static void setup() {}

  private void clickButton() {
    Platform.runLater(() -> {
      Alert info = sut.getInfo();
      if (info != null) {
        DialogPane pane = info.getDialogPane();
        ButtonType yes = pane.getButtonTypes().get(0);
        Button ok = (Button) pane.lookupButton(yes);
        ok.fire();
      }
    });
  }

  private static class TestDbConfigurator extends DbConfigurator {

    public TestDbConfigurator(Stage primaryStage) {
      super(() -> null);
    }

    @Override
    protected FileChooser fileChooser() {
      FileChooser fs = mock(FileChooser.class);
      if (fileSelected) {
        File file = new File("c:/temp/db/test.mv.db");
        when(fs.showOpenDialog(isA(Stage.class))).thenReturn(file);
      }
      return fs;
    }

    @Override
    protected Image getStageIcon() {
      return ImagesMap.get("Citroen.png");
    }

    @Override
    protected void nextAction() {
      next = "nextAction";
    }
  }
}
