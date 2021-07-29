package nl.ealse.ccnl.control.settings;

import java.io.File;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.excelimport.ImportService;
import nl.ealse.ccnl.service.excelimport.ImportService.ImportSelection;
import nl.ealse.ccnl.service.excelimport.ImportType;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ExcelImportController implements ApplicationListener<MenuChoiceEvent> {

  @Value("${ccnl.directory.excel:c:/temp}")
  private String excelDirectory;

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

  public ExcelImportController(PageController pageController, ImportService importService,
      TaskExecutor executor) {
    this.pageController = pageController;
    this.importService = importService;
    this.executor = executor;
  }

  @FXML
  public void initialize() {
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XLSX);
    fileChooser.setInitialDirectory(new File(excelDirectory));
  }

  @FXML
  public void selectFile() {
    selectedFile = fileChooser.showOpenDialog();
    if (selectedFile != null) {
      fileLabel.setText(selectedFile.getAbsolutePath());
      importButton.setDisable(selectCount == 0);
    }

  }

  @FXML
  public void importAllTabs(ActionEvent event) {
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
  public void importTab(ActionEvent event) {
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
  public void importFile() {
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
    AsyncTask asyncTask = new AsyncTask(importService, selection, selectedFile);
    asyncTask.setOnSucceeded(t -> pageController.showMessage("Import succesvol uitgevoerd"));
    asyncTask.setOnFailed(t -> {
      log.error("Import niet succesvol", t.getSource().getException());
      pageController.showErrorMessage("Import niet succesvol");
    });
    executor.execute(asyncTask);
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.IMPORT_FROM_EXCEL == event.getMenuChoice()) {
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
  }

  protected static class AsyncTask extends Task<Void> {

    private final ImportService importService;
    private final ImportSelection selection;
    private final File selectedFile;

    AsyncTask(ImportService importService, ImportSelection selection, File selectedFile) {
      this.importService = importService;
      this.selection = selection;
      this.selectedFile = selectedFile;
    }

    @Override
    protected Void call() throws Exception {
      importService.importFromExcel(selectedFile, selection);
      return null;
    }

  }

}
