package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelimport.ImportService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;

class ExcelImportControllerTest extends FXMLBaseTest<ExcelImportController> {

  private static PageController pageController;
  private static ImportService importService;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor executor = new TestExecutor<ExcelImportController.AsyncTask>();

  private ExcelImportController sut;

  @Test
  void testImport() {
    sut = new ExcelImportController(pageController, importService, executor);
    setDirectory();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      setFileChooser();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

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
    verify(pageController).showMessage("Import succesvol uitgevoerd");
  }

  private void prepare() {
    try {
      getPageWithFxController(sut, PageName.EXCEL_IMPORT);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    importService = mock(ImportService.class);
    fileChooser = mock(WrappedFileChooser.class);
    Resource r = new ClassPathResource("leden.xlsx");
    try {
      when(fileChooser.showOpenDialog()).thenReturn(r.getFile());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  private void setDirectory() {
    try {
      FieldUtils.writeField(sut, "excelDirectory", "C:/temp", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
