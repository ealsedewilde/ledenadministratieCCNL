package nl.ealse.ccnl.control.settings;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.concurrent.Task;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false) // Because no FXML
public class BackupRestoreController implements ApplicationListener<MenuChoiceEvent> {

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

  private static final String FILE_NAME = "backup-%s";

  @Value("${ccnl.directory.db:c:/temp}")
  private String dbDirectory;

  private final PageController pageController;

  private final ApplicationContext springContext;

  private final TaskExecutor executor;

  private BackupRestoreService service;

  private WrappedFileChooser fileChooser;

  public BackupRestoreController(PageController pageController, ApplicationContext springContext,
      TaskExecutor executor) {
    this.pageController = pageController;
    this.springContext = springContext;
    this.executor = executor;
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.MANAGE_BACKUP_DATABASE == event.getMenuChoice()) {
      backupDatabase();
    } else if (MenuChoice.MANAGE_RESTORE_DATABASE == event.getMenuChoice()) {
      restoreDatabase();
    }
  }

  private void backupDatabase() {
    if (fileChooser == null) {
      initialize();
    }
    String fileName = String.format(FILE_NAME, formatter.format(LocalDateTime.now()));
    fileChooser.setInitialFileName(fileName);
    File backupFile = fileChooser.showSaveDialog();
    if (backupFile != null) {
      pageController.showPermanentMessage("Backup wordt aangemaakt; even geduld a.u.b.");
      AsyncTask asyncTask = new AsyncTask(backupFile, service, true);
      asyncTask.setOnSucceeded(t -> pageController.showMessage("Backup is aangemaakt"));
      asyncTask.setOnFailed(t -> pageController.showErrorMessage("Aanmaken backup is mislukt"));
      executor.execute(asyncTask);
    }
  }

  private void restoreDatabase() {
    if (fileChooser == null) {
      initialize();
    }
    fileChooser.setInitialFileName(null);
    File backupFile = fileChooser.showOpenDialog();
    if (backupFile != null) {
      pageController.showPermanentMessage("Backup wordt teruggezet; even geduld a.u.b.");
      AsyncTask asyncTask = new AsyncTask(backupFile, service, false);
      asyncTask.setOnSucceeded(t -> pageController.showMessage("Backup is teruggezet"));
      asyncTask
          .setOnFailed(t -> pageController.showErrorMessage("Terugzetten backup is mislukt"));
      executor.execute(asyncTask);
    }
  }

  private void initialize() {
    service = springContext.getBean(BackupRestoreService.class);
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.ZIP);
    fileChooser.setInitialDirectory(new File(dbDirectory));
  }

  protected static class AsyncTask extends Task<Void> {

    private final BackupRestoreService service;
    private final File backupFile;
    private final boolean backup;

    AsyncTask(File backupFile, BackupRestoreService service, boolean backup) {
      this.backupFile = backupFile;
      this.service = service;
      this.backup = backup;
    }

    @Override
    protected Void call() throws Exception {
      if (backup) {
        service.backupDatabase(backupFile);
        return null;
      }
      service.restoreDatabase(backupFile);
      return null;
    }

  }

}
