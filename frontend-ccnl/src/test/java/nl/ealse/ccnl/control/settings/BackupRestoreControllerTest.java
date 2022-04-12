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
  private static TaskExecutor executor = new TestExecutor<BackupRestoreCommand.AsyncTask>();

  private BackupRestoreCommand sut;

  @Test
  void testController() {
    sut = new BackupRestoreCommand(pageController, service, executor);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    dbDirectory();
    doInitialize();
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_BACKUP_DATABASE);
    sut.executeCommand();
    verify(pageController).showMessage("Backup is aangemaakt");

    event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_RESTORE_DATABASE);
    sut.restore(event);
    verify(pageController).showMessage("Backup is teruggezet");
  }

  @BeforeAll
  static void setup() {
   
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(new File("dummy.zip"));
    when(fileChooser.showOpenDialog()).thenReturn(new File("dummy.zip"));
    pageController = mock(PageController.class);
    service = mock(BackupRestoreService.class);
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
