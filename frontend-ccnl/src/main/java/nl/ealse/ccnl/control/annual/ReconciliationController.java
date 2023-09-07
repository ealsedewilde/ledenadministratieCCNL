package nl.ealse.ccnl.control.annual;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.control.button.ButtonCell;
import nl.ealse.ccnl.control.button.DeleteButton;
import nl.ealse.ccnl.control.exception.AsyncTaskException;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.service.ReconciliationService;
import nl.ealse.javafx.ImagesMap;
import nl.ealse.javafx.util.WrappedFileChooser;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ReconciliationController {

  private final PageController pageController;

  private final ApplicationContext springContext;

  private final TaskExecutor executor;

  private ReconciliationService service;

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

  public ReconciliationController(PageController pageController, ApplicationContext springContext,
      TaskExecutor executor) {
    this.pageController = pageController;
    this.springContext = springContext;
    this.executor = executor;
  }

  @EventListener(condition = "#event.name('RECONCILE_PAYMENTS')")
  public void onApplicationEvent(MenuChoiceEvent event) {
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
    service = springContext.getBean(ReconciliationService.class);
    buttonColumn.setCellFactory(
        t -> new ButtonCell<PaymentFile, String>(new DeleteButton(), this::deleteFile));
    fileChooser = new WrappedFileChooser(pageController.getPrimaryStage(), FileExtension.XML);
    if (messagesStage == null) {
      messagesStage = new Stage();
      messagesStage.setResizable(false);
      messagesStage.setTitle("Aflettermeldingen");
      messagesStage.getIcons().add(ImagesMap.get("info.png"));
      messagesStage.initOwner(pageController.getPrimaryStage());
      Parent parent = pageController.loadPage(PageName.RECONCILE_MESSAGES);
      Scene messagesScene = new Scene(parent, 600, 400);
      messagesStage.setScene(messagesScene);
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
      executor.execute(reconcileTask);
    }
  }

  /**
   * Asynchronous reconciliation.
   */
  protected static class ReconcileTask extends HandledTask {

    private final ReconciliationController controller;
    private final LocalDate referenceDate;
    private final boolean includeDD;

    ReconcileTask(ReconciliationController controller, LocalDate referenceDate, boolean includeDD) {
      super(controller.pageController);
      this.controller = controller;
      this.referenceDate = referenceDate;
      this.includeDD = includeDD;
    }


    @Override
    protected String call() {
      try {
        List<String> messages = controller.service.reconcilePayments(referenceDate, includeDD);
        if (messages != null && !messages.isEmpty()) {
          controller.reconcileMessages.getItems().addAll(messages);
          controller.messagesStage.show();
        }
        return "Betalingen zijn verwerkt";
      } catch (Exception e) {
        log.error("Failed to reconcile", e);
        throw new AsyncTaskException("Verwerking betalingen is mislukt");
      }

    }

  }


}
