package nl.ealse.ccnl.control.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
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
    }));

  }

  @Test
  void testRestoreController() {

    Assertions.assertTrue(runFX(() -> {
      prepare();
      restore();

    }));

  }

  private void backup() {
    setFileChooser();

    sut.backup();
    verify(getPageController()).showMessage("Backup is aangemaakt");
  }

  private void restore() {
    setFileChooser();

    when(service.restoreDatabase(zip)).thenReturn(Boolean.TRUE);
    sut.restore();
    verify(getPageController()).showMessage("Backup is teruggezet");

    when(service.restoreDatabase(zip)).thenReturn(Boolean.FALSE);
    sut.restore();
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
    service = ApplicationContext.getComponent(BackupRestoreService.class);
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
