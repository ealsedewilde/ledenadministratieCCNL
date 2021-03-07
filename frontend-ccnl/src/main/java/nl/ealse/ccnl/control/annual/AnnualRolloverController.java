package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.excel.Archiefbestand;
import nl.ealse.ccnl.ledenadministratie.excel.Ledenbestand;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExportArchiveService;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExportService;
import nl.ealse.ccnl.service.AnnualRolloverService;
import nl.ealse.ccnl.service.BackupRestoreService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AnnualRolloverController implements ApplicationListener<MenuChoiceEvent> {

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

  private WrappedFileChooser fileChooser;

  private File parent;

  @FXML
  private Button backupButton;

  @FXML
  private Button rolloverButton;

  @FXML
  private Button exportButton;

  public AnnualRolloverController(PageController pageController, BackupRestoreService backupService,
      ExportService exportService, AnnualRolloverService rolloverService,
      ExportArchiveService archiveService) {
    this.pageController = pageController;
    this.backupService = backupService;
    this.rolloverService = rolloverService;
    this.exportService = exportService;
    this.archiveService = archiveService;
  }

  @FXML
  public void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.ZIP);
    fileChooser.setInitialDirectory(new File(dbDirectory));
  }

  @FXML
  public void backupDatabase() {
    if (fileChooser == null) {
      initialize();
    }
    String fileName = String.format(BACKUP_FILE_NAME, formatter.format(LocalDateTime.now()));
    fileChooser.setInitialFileName(fileName);
    File backupFile = fileChooser.showSaveDialog();
    if (backupFile != null) {
      parent = backupFile.getParentFile();
      try {
        backupService.backupDatabase(backupFile);
        pageController.setMessage("Backup is aangemaakt");
        backupButton.setDisable(true);
        rolloverButton.setDisable(false);
      } catch (Exception e) {
        pageController.setErrorMessage("Aanmaken backup is mislukt");
      }
    }
  }

  @FXML
  public void annualRollover() {
    rolloverService.annualRollover();
    pageController.setMessage("Jaarovergang is uitgevoerd");
    rolloverButton.setDisable(true);
    exportButton.setDisable(false);
  }

  public void exportToExcel() {
    if (exportMembersToExcel().isPresent() && exportToArchiveExcel().isPresent()) {
      pageController.setMessage("Excel exportbestanden zijn aangemeekt");
    }
  }

  private Optional<Ledenbestand> exportMembersToExcel() {
    String memberFileName = String.format(MEMBER_FILE_NAME, formatter.format(LocalDateTime.now()));
    try {
      return Optional.of(exportService.exportALL(new File(parent, memberFileName)));
    } catch (IOException e) {
      log.error("Could not write Excel document", e);
      pageController.setErrorMessage("Schrijven leden MS Excel-werkblad is mislukt");
    }
    return Optional.empty();
  }

  private Optional<Archiefbestand> exportToArchiveExcel() {
    String archiveFileName =
        String.format(ARCHIVE_FILE_NAME, formatter.format(LocalDateTime.now()));
    try {
      return Optional.of(archiveService.export(new File(parent, archiveFileName)));
    } catch (IOException e) {
      log.error("Could not write Excel document", e);
      pageController.setErrorMessage("Schrijven archief MS Excel-werkblad is mislukt");
    }
    return Optional.empty();
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.ANNUAL_ROLLOVER == event.getMenuChoice()) {
      backupButton.setDisable(false);
      rolloverButton.setDisable(true);
      exportButton.setDisable(true);
    }
  }

}
