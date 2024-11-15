package nl.ealse.ccnl.control.excel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExcelExportCommandTest extends FXMLBaseTest {

  @Mock
  private WrappedFileChooser fileChooser;

  @TempDir
  File tempDir;


  private ExcelExportCommand sut;

  @Test
  void executeCommandTest() {
    File exportFile = new File(tempDir, "export.xlsx");
    when(fileChooser.showSaveDialog()).thenReturn(exportFile);

    final AtomicBoolean ar = new AtomicBoolean();
    runFX(() -> {
      sut = getTestSubject(ExcelExportCommand.class);
      doTest();
      ar.set(true);
    }, ar);
    

  }

  private void doTest() {
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.REPORT_ARCHIVE);
    sut.executeCommand(event);
    verify(getPageController()).showMessage("MS Excel-werkblad voor archief is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_NEW_MEMBERS);
    sut.executeCommand(event);
    verify(getPageController()).showMessage("MS Excel-werkblad voor nieuwe leden is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_CANCELLED_MEMBERS);
    sut.executeCommand(event);
    verify(getPageController()).showMessage("MS Excel-werkblad voor opgezegde leden is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_OVERDUE_MEMBERS);
    sut.executeCommand(event);
    verify(getPageController()).showMessage("MS Excel-werkblad voor niet betalers is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_ALL_DATA);
    sut.executeCommand(event);
    verify(getPageController()).showMessage("MS Excel-werkblad voor alle gegevens is aangemaakt");
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
