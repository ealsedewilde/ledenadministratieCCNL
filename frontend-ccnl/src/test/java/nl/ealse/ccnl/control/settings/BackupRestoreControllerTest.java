package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.ccnl.test.FXBase;
import nl.ealse.ccnl.test.TestExecutor;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;

class BackupRestoreControllerTest extends FXBase {

  private static PageController pageController;
  private static BackupRestoreService service;
  private static WrappedFileChooser fileChooser;
  private static TaskExecutor backupExecutor = new TestExecutor<BackupRestoreCommand.BackupTask>();
  private static TaskExecutor restoreExecutor = new TestExecutor<BackupRestoreCommand.RestoreTask>();
  private static File zip = new File("dummy.zip");
;

  private BackupRestoreCommand sut;

  @Test
  void testBackupController() {
    sut = new BackupRestoreCommand(pageController, service, backupExecutor);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      backup();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  @Test
  void testRestoreController() {
    sut = new BackupRestoreCommand(pageController, service, restoreExecutor);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      restore();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void backup() {
    dbDirectory();
    doInitialize();
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_BACKUP_DATABASE);
    sut.backup(event);
    verify(pageController).showMessage("Backup is aangemaakt");
  }

  private void restore() {
    dbDirectory();
    doInitialize();
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_RESTORE_DATABASE);
    
    when(service.restoreDatabase(zip)).thenReturn(Boolean.TRUE);
    sut.restore(event);
    verify(pageController).showMessage("Backup is teruggezet");

    when(service.restoreDatabase(zip)).thenReturn(Boolean.FALSE);
    sut.restore(event);
    verify(pageController).showErrorMessage("Onjuist bestand; Terugzetten backup is mislukt");
   }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(zip);
    when(fileChooser.showOpenDialog()).thenReturn(zip);
    pageController = mock(PageController.class);
    service = mock(BackupRestoreService.class);
    when(service.restoreDatabase(zip)).thenReturn(Boolean.FALSE);
  }

  private void dbDirectory() {
    try {
      FieldUtils.writeField(sut, "dbDirectory", "C:/temp", true);
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

  private void doInitialize() {
    try {
      MethodUtils.invokeMethod(sut, true, "initialize");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
