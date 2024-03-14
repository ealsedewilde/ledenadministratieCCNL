package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.StageBuilder;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoException;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoProperties;
import nl.ealse.ccnl.service.SepaDirectDebitService;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SepaDirectDebitService.MappingResult;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

/**
 * Controller for genereting DirectDebits file.
 */
@Slf4j
public class SepaDirectDebitsController {
  
  @Getter
  private static final SepaDirectDebitsController instance = new SepaDirectDebitsController();
  
  private final PageController pageController;

  private final SepaDirectDebitService service;

  private File selectedFile;

  private WrappedFileChooser fileChooser;

  private Stage settingsStage;

  private Stage messagesStage;

  @FXML
  private Label errorMessageLabel;

  @FXML
  private Button generateButton;

  // property of directDebitsSettings popup window
  @FXML
  private TableView<FlatProperty> tableView;

  // property of directDebitMessages popup window
  @FXML
  private ListView<String> directDebitMessages;

  @FXML
  private TableColumn<FlatProperty, String> valueColumn;

  @FXML
  private TableColumn<FlatProperty, String> descriptionColumn;

  private SepaDirectDebitsController() {
    this.pageController = PageController.getInstance();
    this.service = SepaDirectDebitService.getInstance();
    setup();
  }
  
  private void setup() {
    messagesStage = new StageBuilder().fxml("dialog/directDebitMessages", this)
        .title("Incassomeldingen").size(600, 400).build();
    settingsStage = new StageBuilder().fxml("dialog/directDebitsSettings", this)
        .title("Incasso instellingen").size(1000, 600).build();
    valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    valueColumn.setOnEditCommit(t -> {
      t.getRowValue().setValue(t.getNewValue());
      saveProperty(t.getRowValue());
    });
    descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    descriptionColumn.setOnEditCommit(t -> {
      t.getRowValue().setDescription(t.getNewValue());
      saveProperty(t.getRowValue());
    });
   fileChooser = new WrappedFileChooser(FileExtension.XML);
    fileChooser.setInitialDirectory(IncassoProperties::getIncassoDirectory);
  }

  @EventListener(menuChoice = MenuChoice.PRODUCE_DIRECT_DEBITS_FILE)
  public void onApplicationEvent(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.DIRECT_DEBITS);
    generateButton.setDisable(true);
    selectedFile = null;
    errorMessageLabel.setVisible(false);
  }

  @FXML
  void manageSettings() {
    tableView.getItems().clear();
    tableView.getItems().addAll(service.getProperties());
    if (!settingsStage.isShowing()) {
      settingsStage.show();
    }
  }

  @FXML
  void selectFile() {
    if (settingsStage.isShowing()) {
      settingsStage.close();
    }
    selectedFile = fileChooser.showSaveDialog();
    if (selectedFile != null) {
      generateButton.setDisable(false);
    }
  }

  @FXML
  void generateDirectDebits() {
    if (messagesStage.isShowing()) {
      messagesStage.close();
    }
    directDebitMessages.getItems().clear();

    pageController.showPermanentMessage("Incassobestand wordt aangemaakt; even geduld a.u.b.");
    DirectDebitTask directDebitTask = new DirectDebitTask(this);
    directDebitTask.executeTask();
    pageController.activateLogoPage();
  }

  @FXML
  public void closeSettings() {
    settingsStage.close();
  }

  protected void saveProperty(FlatProperty newValue) {
    MappingResult result = service.saveProperty(newValue);
    if (!result.isValid()) {
      errorMessageLabel.setText(result.getErrorMessage());
      errorMessageLabel.setVisible(true);
      tableView.getItems().clear();
      tableView.getItems().addAll(service.getProperties());
    } else {
      errorMessageLabel.setVisible(false);
      if (FlatPropertyKey.DD_DIR == newValue.getFpk()) {
        fileChooser.setInitialDirectory(newValue::getValue);
      }
    }
  }

  /**
   * Generate DirectDebits file.
   */
  protected static class DirectDebitTask extends HandledTask {

    private final SepaDirectDebitsController controller;

    private final SepaDirectDebitService service;
    private final File selectedFile;

    DirectDebitTask(SepaDirectDebitsController controller) {
      this.controller = controller;
      this.service = controller.service;
      this.selectedFile = controller.selectedFile;
    }

    @Override
    protected String call() {
      try {
        List<String> messages = service.generateSepaDirectDebitFile(selectedFile);
        if (messages != null && !messages.isEmpty()) {
          controller.directDebitMessages.getItems().addAll(messages);
          controller.messagesStage.show();
        }
        return "Incassobestand is aangemaakt";
      } catch (IncassoException e) {
        log.error("Failed to create file", e);
        throw new AsyncTaskException("Aanmaken incassobestand is mislukt");
      }
    }

  }

}
