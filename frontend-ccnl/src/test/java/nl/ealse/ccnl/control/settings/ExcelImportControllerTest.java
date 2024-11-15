package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.net.URL;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExcelImportControllerTest extends FXMLBaseTest {

  private static WrappedFileChooser fileChooser;

  private ExcelImportController sut;

  @Test
  void testImport() {
    final AtomicBoolean ar = new AtomicBoolean();
    Assertions.assertTrue(runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      setFileChooser();
      doTest();
      ar.set(true);
    }, ar)));
    

  }

  private void doTest() {
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.IMPORT_FROM_EXCEL);
    sut.onApplicationEvent(event);

    sut.selectFile();

    ActionEvent ae = mock(ActionEvent.class);
    CheckBox cb = new CheckBox();
    cb.setSelected(true);
    when(ae.getSource()).thenReturn(cb);
    sut.importAllTabs(ae);
    sut.importTab(ae);

    sut.importFile();
    verify(getPageController()).showMessage("Import succesvol uitgevoerd");
  }

  private void prepare() {
    sut = getTestSubject(ExcelImportController.class);
    getPageWithFxController(sut, PageName.EXCEL_IMPORT);
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    URL url = ExcelImportController.class.getResource("/leden.xlsx");
    when(fileChooser.showOpenDialog()).thenReturn(new File(url.getFile()));
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
