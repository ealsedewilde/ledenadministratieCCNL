package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.AnnualRolloverService;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.ccnl.service.excelexport.ExportArchiveService;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AnnualRolloverController {

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

  private static final String BACKUP_FILE_NAME = "rollover_backup-%s";
  private static final String MEMBER_FILE_NAME = "rollover_leden-%s.xlsx";
  private static final String ARCHIVE_FILE_NAME = "rollover_archief-%s.xlsx";

  @Value("${ccnl.directory.db:c:/temp}")
  private String dbDirectory;

  private final PageController pageController;

  private final BackupRestoreService backupService;

  private final AnnualRolloverService rolloverService;

  private final ExportService exportService;

  private final ExportArchiveService archiveService;

  private final TaskExecutor executor;

  private WrappedFileChooser fileChooser;

  private File parent;

  @FXML
  private Button backupButton;

  @FXML
  private Button rolloverButton;

  @FXML
  private Button exportButton;

  @FXML
  private Label step4;

  public AnnualRolloverController(PageController pageController, BackupRestoreService backupService,
      ExportService exportService, AnnualRolloverService rolloverService,
      ExportArchiveService archiveService, TaskExecutor executor) {
    this.pageController = pageController;
    this.backupService = backupService;
    this.rolloverService = rolloverService;
    this.exportService = exportService;
    this.archiveService = archiveService;
    this.executor = executor;
  }

  @FXML
  public void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.ZIP);
    fileChooser.setInitialDirectory(new File(dbDirectory));
  }

  /**
   * Step 1 of the rollover process.
   */
  @FXML
  public void backupDatabase() {
    if (fileChooser == null) {
      initialize();
    }
    String fileName = String.format(BACKUP_FILE_NAME, formatter.format(LocalDateTime.now()));
    fileChooser.setInitialFileName(fileName);
    File backupFile = fileChooser.showSaveDialog();
    if (backupFile != null) {
      pageController.showPermanentMessage("Backup wordt aangemaakt; even geduld a.u.b.");
      AsyncRolloverStep1 asyncTask = new AsyncRolloverStep1(this, backupFile);
      asyncTask
          .setOnSucceeded(t -> pageController.showMessage(t.getSource().getValue().toString()));
      asyncTask.setOnFailed(
          evt -> pageController.showErrorMessage(evt.getSource().getException().getMessage()));
      executor.execute(asyncTask);
      backupButton.setDisable(true);
      rolloverButton.setDisable(false);
    }
  }

  /**
   * Step 2 of the rollover process.
   */
  @FXML
  public void annualRollover() {
    pageController.showPermanentMessage("Jaarovergang wordt uitgevoerd; even geduld a.u.b.");
    AsyncRolloverStep2 asyncTask = new AsyncRolloverStep2(this);
    asyncTask
        .setOnSucceeded(evt -> pageController.showMessage(evt.getSource().getValue().toString()));
    asyncTask.setOnFailed(
        evt -> pageController.showErrorMessage(evt.getSource().getException().getMessage()));
    executor.execute(asyncTask);
    rolloverButton.setDisable(true);
    exportButton.setDisable(false);
  }

  /**
   * Step 3 of the rollover process.
   */
  @FXML
  public void exportToExcel() {
    pageController.showPermanentMessage("Excel export wordt aangemaakt; even geduld a.u.b.");
    AsyncRolloverStep3 asyncTask = new AsyncRolloverStep3(this);
    asyncTask.setOnSucceeded(evt -> {
      pageController.showMessage(evt.getSource().getValue().toString());
      step4.setText("Stap 4: Controleer handmatig de Excel exportbestanden in map " + parent);
    });
    asyncTask.setOnFailed(
        evt -> pageController.showErrorMessage(evt.getSource().getException().getMessage()));
    executor.execute(asyncTask);
  }

  @EventListener(condition = "#event.name('ANNUAL_ROLLOVER')")
  public void onApplicationEvent(MenuChoiceEvent event) {
    backupButton.setDisable(false);
    rolloverButton.setDisable(true);
    exportButton.setDisable(true);
  }

  protected static class AsyncRolloverStep1 extends Task<String> {
    private final AnnualRolloverController controller;
    private final File backupFile;

    AsyncRolloverStep1(AnnualRolloverController controller, File backupFile) {
      this.controller = controller;
      this.backupFile = backupFile;
    }

    @Override
    protected String call() {
      controller.parent = backupFile.getParentFile();
      try {
        controller.backupService.backupDatabase(backupFile);
        controller.backupButton.setDisable(true);
        controller.rolloverButton.setDisable(false);
        return "Backup is aangemaakt";
      } catch (Exception e) {
        log.error("Database backup failed", e);
        throw new AsyncTaskException("Aanmaken backup is mislukt");
      }
    }
  }

  protected static class AsyncRolloverStep2 extends Task<String> {
    private final AnnualRolloverController controller;

    AsyncRolloverStep2(AnnualRolloverController controller) {
      this.controller = controller;
    }

    @Override
    protected String call() {
      controller.rolloverService.annualRollover();
      return "Jaarovergang is uitgevoerd";
    }

  }

  protected static class AsyncRolloverStep3 extends Task<String> {
    private final AnnualRolloverController controller;

    AsyncRolloverStep3(AnnualRolloverController controller) {
      this.controller = controller;
    }

    @Override
    protected String call() {
      exportMembersToExcel();
      exportToArchiveExcel();
      return "Excel exportbestanden zijn aangemaakt";
    }

    private void exportMembersToExcel() {
      String memberFileName =
          String.format(MEMBER_FILE_NAME, formatter.format(LocalDateTime.now()));
      try {
        controller.exportService.exportALL(new File(controller.parent, memberFileName));
      } catch (IOException e) {
        log.error("Could not write Excel document", e);
        throw new AsyncTaskException("Schrijven leden MS Excel-werkblad is mislukt");
      }
    }

    private void exportToArchiveExcel() {
      String archiveFileName =
          String.format(ARCHIVE_FILE_NAME, formatter.format(LocalDateTime.now()));
      try {
        controller.archiveService.export(new File(controller.parent, archiveFileName));
      } catch (IOException e) {
        log.error("Could not write Excel document", e);
        throw new AsyncTaskException("Schrijven archief MS Excel-werkblad is mislukt");
      }
    }
  }

}
