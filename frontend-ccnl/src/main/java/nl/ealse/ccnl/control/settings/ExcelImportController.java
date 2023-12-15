package nl.ealse.ccnl.control.settings;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler.ImportSelection;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportType;
import nl.ealse.ccnl.service.excelimport.ImportService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class ExcelImportController {

  @Getter
  private static final ExcelImportController instance = new ExcelImportController();

  private final PageController pageController;

  private final ImportService importService;

  private final TaskExecutor executor;


  @FXML
  private ToggleGroup importGroup;

  @FXML
  private CheckBox all;

  @FXML
  private CheckBox members;
  @FXML
  private CheckBox partners;
  @FXML
  private CheckBox clubs;
  @FXML
  private CheckBox external;
  @FXML
  private CheckBox internal;

  @FXML
  private Label fileLabel;

  @FXML
  private Button importButton;

  private int selectCount;

  private WrappedFileChooser fileChooser;

  private File selectedFile;

  private ExcelImportController() {
    this.pageController = PageController.getInstance();
    this.importService = ImportService.getInstance();
    this.executor = TaskExecutor.getInstance();
    setup();
  }

  private void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.XLSX);
    fileChooser.setInitialDirectory(
        () -> DatabaseProperties.getProperty("ccnl.directory.excel", "c:/temp"));
  }

  @FXML
  void selectFile() {
    selectedFile = fileChooser.showOpenDialog();
    if (selectedFile != null) {
      fileLabel.setText(selectedFile.getAbsolutePath());
      importButton.setDisable(selectCount == 0);
    }

  }

  @FXML
  void importAllTabs(ActionEvent event) {
    CheckBox src = (CheckBox) event.getSource();
    if (src.isSelected()) {
      selectCount = 5;
    } else {
      selectCount = 0;
    }
    members.setSelected(src.isSelected());
    partners.setSelected(src.isSelected());
    clubs.setSelected(src.isSelected());
    external.setSelected(src.isSelected());
    internal.setSelected(src.isSelected());
    importButton.setDisable(selectedFile == null || selectCount == 0);
  }

  @FXML
  void importTab(ActionEvent event) {
    CheckBox src = (CheckBox) event.getSource();
    if (src.isSelected()) {
      selectCount++;
    } else {
      selectCount--;
    }
    all.setSelected(selectCount == 5);
    importButton.setDisable(selectedFile == null || selectCount == 0);
  }

  @FXML
  void importFile() {
    ImportType type = ImportType.REPLACE;
    for (Toggle t : importGroup.getToggles()) {
      if (t.isSelected()) {
        ToggleButton b = (ToggleButton) t;
        type = ImportType.fromId(b.getId());
        break;
      }
    }
    ImportSelection selection = new ImportSelection(members.isSelected(), partners.isSelected(),
        clubs.isSelected(), external.isSelected(), internal.isSelected(), type);
    pageController.showPermanentMessage("Import wordt uitgevoerd; even geduld a.u.b.");
    AsyncTask asyncTask = new AsyncTask(this, selection);
    executor.execute(asyncTask);
  }

  @EventListener(menuChoice = MenuChoice.IMPORT_FROM_EXCEL)
  public void onApplicationEvent(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.EXCEL_IMPORT);
    selectedFile = null;
    fileLabel.setText("");
    importButton.setDisable(true);
    importGroup.getToggles().get(2).setSelected(true);
    selectCount = 0;
    all.setSelected(false);
    members.setSelected(false);
    partners.setSelected(false);
    clubs.setSelected(false);
    external.setSelected(false);
    internal.setSelected(false);
  }

  protected static class AsyncTask extends HandledTask {

    private final ImportService importService;
    private final ImportSelection selection;
    private final File selectedFile;

    AsyncTask(ExcelImportController controller, ImportSelection selection) {
      super(controller.pageController);
      this.importService = controller.importService;
      this.selection = selection;
      this.selectedFile = controller.selectedFile;
    }

    @Override
    protected String call() {
      try {
        importService.importFromExcel(selectedFile, selection);
        return "Import succesvol uitgevoerd";
      } catch (Exception e) {
        log.error("Import failed", e);
        throw new AsyncTaskException("Import is mislukt");
      }

    }

  }

}
