package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ioc.ComponentProviderUtil;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BackupRestoreControllerTest extends FXMLBaseTest {

  private static BackupRestoreService service;
  private static WrappedFileChooser fileChooser;
  private static File zip = new File("dummy.zip");;

  private static BackupRestoreCommand sut;

  @Test
  void testBackupController() {

    Assertions.assertTrue(runFX(() -> {
      prepare();
      backup();
      return Boolean.TRUE;
    }));
    
  }

  @Test
  void testRestoreController() {

    Assertions.assertTrue(runFX(() -> {
      prepare();
      restore();
      return Boolean.TRUE;
    }));
    
  }

  private void backup() {
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_BACKUP_DATABASE);
    sut.backup(event);
    verify(getPageController()).showMessage("Backup is aangemaakt");
  }

  private void restore() {
    setFileChooser();

    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.MANAGE_RESTORE_DATABASE);

    when(service.restoreDatabase(zip)).thenReturn(Boolean.TRUE);
    sut.restore(event);
    verify(getPageController()).showMessage("Backup is teruggezet");

    when(service.restoreDatabase(zip)).thenReturn(Boolean.FALSE);
    sut.restore(event);
    verify(getPageController()).showErrorMessage("Onjuist bestand; Terugzetten backup is mislukt");
  }

  private void prepare() {
    sut = getTestSubject(BackupRestoreCommand.class);
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(zip);
    when(fileChooser.showOpenDialog()).thenReturn(zip);
    service = ComponentProviderUtil.getComponent(BackupRestoreService.class);
    when(service.restoreDatabase(zip)).thenReturn(Boolean.FALSE);
  }


  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
