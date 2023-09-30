package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.annual.AnnualRolloverController.AsyncRolloverStep1;
import nl.ealse.ccnl.control.annual.AnnualRolloverController.AsyncRolloverStep2;
import nl.ealse.ccnl.control.annual.AnnualRolloverController.AsyncRolloverStep3;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.AnnualRolloverService;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.ccnl.service.excelexport.ExportArchiveService;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.task.TaskExecutor;

class AnnualRolloverControllerTest extends FXMLBaseTest<AnnualRolloverController> {

  private static PageController pageController;
  private static BackupRestoreService backupService;
  private static AnnualRolloverService rolloverService;
  private static ExportService exportService;
  private static ExportArchiveService archiveService;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor step1Executor = new TestExecutor<AsyncRolloverStep1>();
  private static TaskExecutor step2Executor = new TestExecutor<AsyncRolloverStep2>();
  private static TaskExecutor step3Executor = new TestExecutor<AsyncRolloverStep3>();
  private static TaskExecutor executor = task -> {
    if ( task instanceof AsyncRolloverStep1) {
      step1Executor.execute(task);
    } else if ( task instanceof AsyncRolloverStep2) {
      step2Executor.execute(task);
    } else {
      step3Executor.execute(task);
    }
  };

  @TempDir
  File tempDir;

  private AnnualRolloverController sut;

  @Test
  void testController() {
    sut = new AnnualRolloverController(pageController, backupService, exportService,
        rolloverService, archiveService, executor);
    dbDirectory();
    File backupFile = new File(tempDir, "backup.zip");
    when(fileChooser.showSaveDialog()).thenReturn(backupFile);
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
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.ANNUAL_ROLLOVER);
    sut.onApplicationEvent(event);

    sut.backupDatabase();
    verify(pageController).showMessage("Backup is aangemaakt");

    sut.annualRollover();
    verify(pageController).showMessage("Jaarovergang is uitgevoerd");

    sut.exportToExcel();
    verify(pageController).showMessage("Excel exportbestanden zijn aangemaakt");
  }

  private void prepare() {
    try {
      getPageWithFxController(sut, PageName.ANNUAL_ROLLOVER);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
    pageController = mock(PageController.class);
    backupService = mock(BackupRestoreService.class);
    rolloverService = mock(AnnualRolloverService.class);
    exportService = mock(ExportService.class);
    archiveService = mock(ExportArchiveService.class);
    fileChooser = mock(WrappedFileChooser.class);
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void dbDirectory() {
    try {
      FieldUtils.writeDeclaredField(sut, "dbDirectory", tempDir.getAbsolutePath(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
