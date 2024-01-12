package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.TaskExecutor;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.service.AnnualRolloverService;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.ccnl.service.excelexport.ExportArchiveService;
import nl.ealse.ccnl.service.excelexport.ExportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

/**
 * Controller for the annual rollover the the new club year.
 */
@Slf4j
public class AnnualRolloverController {

  @Getter
  private static final AnnualRolloverController instance = new AnnualRolloverController();

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

  private static final String DEFAULT_DIR = "c:/temp";

  private static final String BACKUP_FILE_NAME = "rollover_backup-%s";
  private static final String MEMBER_FILE_NAME = "rollover_leden-%s.xlsx";
  private static final String ARCHIVE_FILE_NAME = "rollover_archief-%s.xlsx";

  private final PageController pageController;

  private final BackupRestoreService backupService;

  private final AnnualRolloverService rolloverService;

  private final ExportService exportService;

  private final ExportArchiveService archiveService;

  private final TaskExecutor executor;

  private WrappedFileChooser fileChooser;

  @FXML
  private Button backupButton;

  @FXML
  private Button rolloverButton;

  @FXML
  private Button exportButton;

  @FXML
  private Label step4;

  private AnnualRolloverController() {
    this.pageController = PageController.getInstance();
    this.backupService = BackupRestoreService.getInstance();
    this.rolloverService = AnnualRolloverService.getInstance();
    this.exportService = ExportService.getInstance();
    this.archiveService = ExportArchiveService.getInstance();
    this.executor = TaskExecutor.getInstance();
    setup();
  }

  private void setup() {
    this.fileChooser = new WrappedFileChooser(FileExtension.ZIP);
    this.fileChooser.setInitialDirectory(
        () -> DatabaseProperties.getProperty("ccnl.directory.db", DEFAULT_DIR));
  }

  /**
   * Step 1 of the rollover process.
   */
  @FXML
  void backupDatabase() {
    String fileName = String.format(BACKUP_FILE_NAME, formatter.format(LocalDateTime.now()));
    fileChooser.setInitialFileName(fileName);
    File backupFile = fileChooser.showSaveDialog();
    if (backupFile != null) {
      pageController.showPermanentMessage("Backup wordt aangemaakt; even geduld a.u.b.");
      AsyncRolloverStep1 asyncTask = new AsyncRolloverStep1(this, backupFile);
      executor.execute(asyncTask);
      backupButton.setDisable(true);
      rolloverButton.setDisable(false);
    }
  }

  /**
   * Step 2 of the rollover process.
   */
  @FXML
  void annualRollover() {
    pageController.showPermanentMessage("Jaarovergang wordt uitgevoerd; even geduld a.u.b.");
    AsyncRolloverStep2 asyncTask = new AsyncRolloverStep2(this);
    executor.execute(asyncTask);
    rolloverButton.setDisable(true);
    exportButton.setDisable(false);
  }

  /**
   * Step 3 of the rollover process.
   */
  @FXML
  void exportToExcel() {
    pageController.showPermanentMessage("Excel export wordt aangemaakt; even geduld a.u.b.");
    AsyncRolloverStep3 asyncTask = new AsyncRolloverStep3(this);
    executor.execute(asyncTask);
  }

  @EventListener(menuChoice = MenuChoice.ANNUAL_ROLLOVER)
  public void onApplicationEvent(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.ANNUAL_ROLLOVER);
    backupButton.setDisable(false);
    rolloverButton.setDisable(true);
    exportButton.setDisable(true);
  }

  protected static class AsyncRolloverStep1 extends HandledTask {
    private final AnnualRolloverController controller;
    private final File backupFile;

    AsyncRolloverStep1(AnnualRolloverController controller, File backupFile) {
      this.controller = controller;
      this.backupFile = backupFile;
    }

    @Override
    protected String call() {
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

  protected static class AsyncRolloverStep2 extends HandledTask {
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

  protected static class AsyncRolloverStep3 extends HandledTask {
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
        File excelParent =
            new File(DatabaseProperties.getProperty("ccnl.directory.annual", DEFAULT_DIR));
        controller.exportService.exportALL(new File(excelParent, memberFileName));
      } catch (IOException e) {
        log.error("Could not write Excel document", e);
        throw new AsyncTaskException("Schrijven leden MS Excel-werkblad is mislukt");
      }
    }

    private void exportToArchiveExcel() {
      String archiveFileName =
          String.format(ARCHIVE_FILE_NAME, formatter.format(LocalDateTime.now()));
      try {
        File excelParent =
            new File(DatabaseProperties.getProperty("ccnl.directory.annual", DEFAULT_DIR));
        controller.archiveService.export(new File(excelParent, archiveFileName));
      } catch (IOException e) {
        log.error("Could not write Excel document", e);
        throw new AsyncTaskException("Schrijven archief MS Excel-werkblad is mislukt");
      }
    }
  }

}
