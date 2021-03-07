package nl.ealse.ccnl.control.annual;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.excel.Archiefbestand;
import nl.ealse.ccnl.ledenadministratie.excel.Ledenbestand;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExportArchiveService;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExportService;
import nl.ealse.ccnl.service.AnnualRolloverService;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AnnualRolloverControllerTest extends FXMLBaseTest<AnnualRolloverController> {

  private static PageController pageController;
  private static BackupRestoreService backupService;
  private static AnnualRolloverService rolloverService;
  private static ExportService exportService;
  private static ExportArchiveService archiveService;
  private static WrappedFileChooser fileChooser;

  @TempDir
  File tempDir;

  private AnnualRolloverController sut;

  @Test
  void testController() {
    sut = new AnnualRolloverController(pageController, backupService, exportService,
        rolloverService, archiveService);
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
    verify(pageController).setMessage("Backup is aangemaakt");

    sut.annualRollover();
    verify(pageController).setMessage("Jaarovergang is uitgevoerd");

    sut.exportToExcel();
    verify(pageController).setMessage("Excel exportbestanden zijn aangemeekt");
  }

  private void prepare() {
    try {
      getPage(sut, PageName.ANNUAL_ROLLOVER);
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
    Ledenbestand lb = mock(Ledenbestand.class);
    archiveService = mock(ExportArchiveService.class);
    Archiefbestand ab = mock(Archiefbestand.class);
    try {
      when(exportService.exportALL(any(File.class))).thenReturn(lb);
      when(archiveService.export(any(File.class))).thenReturn(ab);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
      Field f = sut.getClass().getDeclaredField("dbDirectory");
      f.setAccessible(true);
      f.set(sut, tempDir.getAbsolutePath());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
