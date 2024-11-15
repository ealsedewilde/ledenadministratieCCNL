package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AnnualRolloverControllerTest extends FXMLBaseTest {

  private static WrappedFileChooser fileChooser;

  @TempDir
  File tempDir;

  private AnnualRolloverController sut;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    
  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.ANNUAL_ROLLOVER);
    sut.onApplicationEvent(event);

    sut.backupDatabase();
    verify(getPageController()).showMessage("Backup is aangemaakt");

    sut.annualRollover();
    verify(getPageController()).showMessage("Jaarovergang is uitgevoerd");

    sut.exportToExcel();
    verify(getPageController()).showMessage("Excel exportbestanden zijn aangemaakt in c:/temp");
  }

  private void prepare() {
    sut = getTestSubject(AnnualRolloverController.class);
    setFileChooser();
    File backupFile = new File(tempDir, "backup.zip");
    when(fileChooser.showSaveDialog()).thenReturn(backupFile);
    getPageWithFxController(sut, PageName.ANNUAL_ROLLOVER);
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
