package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.dd.IncassoException;
import nl.ealse.ccnl.service.SepaDirectDebitService;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatProperty;
import nl.ealse.ccnl.service.SepaDirectDebitService.FlatPropertyKey;
import nl.ealse.ccnl.service.SepaDirectDebitService.MappingResult;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

@Controller
public class SepaDirectDebitsController implements ApplicationListener<MenuChoiceEvent> {
  private final PageController pageController;

  private final SepaDirectDebitService service;

  private File selectedFile;

  private WrappedFileChooser fileChooser;

  private Stage dialog;

  @FXML
  private Label errorMessageLabel;

  @FXML
  private Button generateButton;

  @FXML
  private TableView<FlatProperty> tableView;

  @FXML
  private ListView<String> directDebitMessages;

  @FXML
  private TableColumn<FlatProperty, String> valueColumn;

  @FXML
  private TableColumn<FlatProperty, String> descriptionColumn;
  
  private Stage messagesStage;


  public SepaDirectDebitsController(SepaDirectDebitService service, PageController pageController) {
    this.pageController = pageController;
    this.service = service;
  }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.PRODUCE_DIRECT_DEBITS_FILE == event.getMenuChoice()) {
      generateButton.setDisable(true);
      selectedFile = null;
      tableView.getItems().clear();
      tableView.getItems().addAll(service.getProperties());
      errorMessageLabel.setVisible(false);
    }
  }

  @FXML
  public void initialize() {
    if (dialog == null) {
      fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XML);
      fileChooser.setInitialDirectory(service.getDirectDebitsDirectory());

      dialog = new Stage();
      dialog.setResizable(false);
      dialog.setTitle("Incasso instellingen");
      dialog.getIcons().add(ImagesMap.get("info.png"));

      dialog.initOwner(pageController.getPrimaryStage());
      Parent parent = pageController.loadPage(PageName.DIRECT_DEBITS_SETTINGS);
      Scene dialogScene = new Scene(parent, 1000, 600);
      dialog.setScene(dialogScene);

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
    }
    if (messagesStage == null) {
      messagesStage = new Stage();
      messagesStage.setResizable(false);
      messagesStage.setTitle("Incassomeldingen");
      messagesStage.getIcons().add(ImagesMap.get("info.png"));
      messagesStage.initOwner(pageController.getPrimaryStage());
      Parent parent = pageController.loadPage(PageName.DIRECT_DEBITS_MESSAGES);
      Scene messagesScene = new Scene(parent, 600, 400);
      messagesStage.setScene(messagesScene);
    }

  }

  @FXML
  public void manageSettings() {
    if (!dialog.isShowing()) {
      dialog.show();
    }
  }

  @FXML
  public void selectFile() {
    selectedFile = fileChooser.showSaveDialog();
    if (selectedFile != null) {
      generateButton.setDisable(false);
    }
  }

  @FXML
  public void generateDirectDebits() {
    try {
      List<String> messages = service.generateSepaDirectDebitFile(selectedFile);
      if (!messages.isEmpty()) {
        directDebitMessages.getItems().clear();
        directDebitMessages.getItems().addAll(messages);
        messagesStage.show();
      }
      pageController.setMessage("Incassobestand is aangemaakt");
    } catch (IncassoException e) {
      pageController.setErrorMessage("Aanmaken incassobestand is mislukt");
    }
    pageController.setActivePage(PageName.LOGO);
  }

  @FXML
  public void closeSettings() {
    dialog.close();
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
        fileChooser.setInitialDirectory(new File(newValue.getValue()));
      }
    }
  }

}
