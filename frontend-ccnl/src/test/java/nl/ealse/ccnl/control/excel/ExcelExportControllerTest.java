package nl.ealse.ccnl.control.excel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.excel.ExcelExportController.AsyncArchiveTask;
import nl.ealse.ccnl.control.excel.ExcelExportController.AsyncExportTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelexport.ExportArchiveService;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

@ExtendWith(MockitoExtension.class)
class ExcelExportControllerTest extends FXBase {

  @Mock
  private PageController pageController;

  @Mock
  private ApplicationContext springContext;

  @Mock
  private ExportService exportService;

  @Mock
  private ExportArchiveService exportArchiveService;

  @Mock
  private WrappedFileChooser fileChooser;

  @TempDir
  File tempDir;
  
  private static TaskExecutor defaultExecutor = new TestExecutor<AsyncExportTask>();
  private static TaskExecutor archiveExecutor = new TestExecutor<AsyncArchiveTask>();
  private static TaskExecutor executor = (task -> {
    if (task instanceof AsyncArchiveTask) {
      archiveExecutor.execute(task);
    } else {
      defaultExecutor.execute(task);
    }
  });

  private ExcelExportController sut;

  @Test
  void onEventTest() {

    when(springContext.getBean(ExportService.class)).thenReturn(exportService);
    when(springContext.getBean(ExportArchiveService.class)).thenReturn(exportArchiveService);
    File exportFile = new File(tempDir, "export.xlsx");
    when(fileChooser.showSaveDialog()).thenReturn(exportFile);
    sut = new ExcelExportController(pageController, springContext, executor);
    setDirectory();

    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

  }

  private void doTest() {
    doInitialize();
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.REPORT_ARCHIVE);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("MS Excel-werkblad voor archief is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_NEW_MEMBERS);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("MS Excel-werkblad voor nieuwe leden is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_CANCELLED_MEMBERS);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("MS Excel-werkblad voor opgezegde leden is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_OVERDUE_MEMBERS);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("MS Excel-werkblad voor niet betalers is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.REPORT_ALL_DATA);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("MS Excel-werkblad voor alle gegevens is aangemaakt");
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setDirectory() {
    try {
      FieldUtils.writeField(sut, "excelDirectory", tempDir.getAbsolutePath(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void doInitialize() {
    try {
      MethodUtils.invokeMethod(sut, true, "initialize");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
