package nl.ealse.ccnl.control.settings;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class BackupRestoreCommand {

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

  private static final String FILE_NAME = "backup-%s";

  @Value("${ccnl.directory.db:c:/temp}")
  private String dbDirectory;

  private final PageController pageController;

  private final TaskExecutor executor;

  private final BackupRestoreService service;

  private WrappedFileChooser fileChooser;

  public BackupRestoreCommand(PageController pageController, BackupRestoreService service,
      TaskExecutor executor) {
    this.pageController = pageController;
    this.service = service;
    this.executor = executor;
  }
  
  @PostConstruct
  void setup() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.ZIP);
    fileChooser.setInitialDirectory(new File(dbDirectory));
  }

  @EventListener(condition = "#event.name('MANAGE_BACKUP_DATABASE')")
  public void backup(MenuChoiceEvent event) {
    String fileName = String.format(FILE_NAME, formatter.format(LocalDateTime.now()));
    fileChooser.setInitialFileName(fileName);
    File backupFile = fileChooser.showSaveDialog();
    if (backupFile != null) {
      pageController.showPermanentMessage("Backup wordt aangemaakt; even geduld a.u.b.");
      BackupTask asyncTask = new BackupTask(this, backupFile);
      executor.execute(asyncTask);
    }
  }

  @EventListener(condition = "#event.name('MANAGE_RESTORE_DATABASE')")
  public void restore(MenuChoiceEvent event) {
    fileChooser.setInitialFileName(null);
    File backupFile = fileChooser.showOpenDialog();
    if (backupFile != null) {
      pageController.showPermanentMessage("Backup wordt teruggezet; even geduld a.u.b.");
      RestoreTask asyncTask = new RestoreTask(this, backupFile);
      executor.execute(asyncTask);
    }
  }

  private void initialize() {
  }

  protected static class BackupTask extends HandledTask {

    private final BackupRestoreService service;
    private final File backupFile;

    BackupTask(BackupRestoreCommand command, File backupFile) {
      super(command.pageController);
      this.backupFile = backupFile;
      this.service = command.service;
    }

    @Override
    protected String call() {
      try {
        service.backupDatabase(backupFile);
        return "Backup is aangemaakt";
      } catch (Exception e) {
        log.error("Creating backup failed, e");
        throw new AsyncTaskException("Aanmaken backup is mislukt");
      }
    }

  }
  protected static class RestoreTask extends HandledTask {

    private final BackupRestoreService service;
    private final File backupFile;

    RestoreTask(BackupRestoreCommand command, File backupFile) {
      super(command.pageController);
      this.backupFile = backupFile;
      this.service = command.service;
    }

    @Override
    protected String call() {
      try {
        if (service.restoreDatabase(backupFile)) {
          return "Backup is teruggezet";
        } 
      } catch (Exception e) {
        log.error("Restoring backup failed, e");
        throw new AsyncTaskException("Terugzetten backup is mislukt");
      }
      throw new AsyncTaskException("Onjuist bestand; Terugzetten backup is mislukt");
    }

  }

}
