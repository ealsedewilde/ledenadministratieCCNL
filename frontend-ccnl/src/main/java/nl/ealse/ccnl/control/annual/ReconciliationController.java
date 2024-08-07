package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.AsyncTaskException;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.StageBuilder;
import nl.ealse.ccnl.control.button.ButtonCell;
import nl.ealse.ccnl.control.button.DeleteButton;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.service.ReconciliationService;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public class ReconciliationController {

  private final PageController pageController;

  private final ReconciliationService service;

  private WrappedFileChooser fileChooser;

  @FXML
  private TableView<PaymentFile> tableView;

  // Property of reconciliationMessages popup window
  @FXML
  private ListView<String> reconcileMessages;

  @FXML
  private TableColumn<PaymentFile, String> buttonColumn;

  @FXML
  private CheckBox includeDD;

  @FXML
  private DatePicker referenceDate;

  @FXML
  private Label referenceDateE;

  private Stage messagesStage;

  public ReconciliationController(PageController pageController, ReconciliationService service) {
    this.pageController = pageController;
    this.service = service;
    setup();
  }

  private void setup() {
    fileChooser = new WrappedFileChooser(FileExtension.XML);
    messagesStage = new StageBuilder().fxml("dialog/reconciliationMessages", this)
        .title("Aflettermeldingen").size(600, 400).build();
  }

  @EventListener(menuChoice = MenuChoice.RECONCILE_PAYMENTS)
  public void onApplicationEvent(MenuChoiceEvent event) {
    pageController.setActivePage(PageName.RECONCILE_PAYMENTS);
    tableView.getItems().clear();
    tableView.getItems().addAll(service.allFiles());
    LocalDate d = LocalDate.now();
    int year = d.getYear() - 1;
    d = LocalDate.of(year, 12, 1);
    referenceDate.setValue(d);
    referenceDateE.setVisible(false);
    if (messagesStage.isShowing()) {
      messagesStage.close();
    }
  }

  @FXML
  void initialize() {
    if (buttonColumn != null) {
      buttonColumn.setCellFactory(
          t -> new ButtonCell<PaymentFile, String>(new DeleteButton(), this::deleteFile));
    }
  }

  @FXML
  void selectFile() {
    File selectedFile = fileChooser.showOpenDialog();
    if (selectedFile != null) {
      try {
        if (service.saveFile(selectedFile)) {
          tableView.getItems().clear();
          tableView.getItems().addAll(service.allFiles());
        } else {
          pageController.showErrorMessage("Bestand is geen geldig 'camt.053' betalingenbestand");
        }
      } catch (IOException e) {
        pageController.showErrorMessage("Inlezen bestand is mislukt");
      }
    }
  }

  @FXML
  void deleteFile(ActionEvent e) {
    Button b = (Button) e.getSource();
    @SuppressWarnings("unchecked")
    TableCell<PaymentFile, String> cell = (TableCell<PaymentFile, String>) b.getParent();
    PaymentFile fileToDelete = cell.getTableRow().getItem();
    service.deleteFile(fileToDelete);
    tableView.getItems().clear();
    tableView.getItems().addAll(service.allFiles());
  }

  @FXML
  void reconcilePayments() {
    if (messagesStage.isShowing()) {
      messagesStage.close();
    }
    reconcileMessages.getItems().clear();

    LocalDate dateValue = referenceDate.getValue();
    if (dateValue == null) {
      referenceDateE.setVisible(true);
    } else {
      referenceDateE.setVisible(false);
      pageController.showPermanentMessage("Betalingen worden verwerkt; even geduld a.u.b.");
      ReconcileTask reconcileTask = new ReconcileTask(this, dateValue, includeDD.isSelected());
      reconcileTask.setOnSucceeded(evt -> {
        pageController.showMessage(evt.getSource().getValue().toString());
        List<String> messages = reconcileTask.getMessages();
        if (messages != null && !messages.isEmpty()) {
          reconcileMessages.getItems().addAll(messages);
          messagesStage.show();
        }
      });

      reconcileTask.executeTask();
    }
  }

  /**
   * Asynchronous reconciliation.
   */
  protected static class ReconcileTask extends HandledTask {

    private final ReconciliationController controller;
    private final LocalDate referenceDate;
    private final boolean includeDD;
    @Getter
    private List<String> messages;

    ReconcileTask(ReconciliationController controller, LocalDate referenceDate, boolean includeDD) {
      this.controller = controller;
      this.referenceDate = referenceDate;
      this.includeDD = includeDD;
    }


    @Override
    protected String call() {
      try {
        messages = controller.service.reconcilePayments(referenceDate, includeDD);
        return "Betalingen zijn verwerkt";
      } catch (Exception e) {
        log.error("Failed to reconcile", e);
        throw new AsyncTaskException("Verwerking betalingen is mislukt");
      }

    }

  }


}
