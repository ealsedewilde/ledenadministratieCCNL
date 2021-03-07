package nl.ealse.ccnl.control.settings;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false) // Because no FXM
public class BackupRestoreController implements ApplicationListener<MenuChoiceEvent> {

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

  private static final String FILE_NAME = "backup-%s";

  @Value("${ccnl.directory.db:c:/temp}")
  private String dbDirectory;

  private final PageController pageController;

  private final ApplicationContext springContext;

  private BackupRestoreService service;

  private WrappedFileChooser fileChooser;

  public BackupRestoreController(PageController pageController, ApplicationContext springContext) {
    this.pageController = pageController;
    this.springContext = springContext;
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
      try {
        service.backupDatabase(backupFile);
        pageController.setMessage("Backup is aangemaakt");
      } catch (Exception e) {
        pageController.setErrorMessage("Aanmaken backup is mislukt");
      }
    }
  }

  private void restoreDatabase() {
    if (fileChooser == null) {
      initialize();
    }
    fileChooser.setInitialFileName(null);
    File backupFile = fileChooser.showOpenDialog();
    if (backupFile != null) {
      try {
        pageController.setMessage("Backup wordt teruggezet; even geduld a.u.b.");
        service.restoreDatabase(backupFile);
        pageController.setMessage("Backup is teruggezet");
      } catch (Exception e) {
        pageController.setErrorMessage("Terugzetten backup is mislukt");
      }
    }
  }

  private void initialize() {
    service = springContext.getBean(BackupRestoreService.class);
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.ZIP);
    fileChooser.setInitialDirectory(new File(dbDirectory));
  }

}
