package nl.ealse.ccnl.control.annual;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.support.EventListener;
import nl.ealse.ccnl.service.ReconciliationService;

public class ResetPaymentCommand {

  private final PageController pageController;

  private final ReconciliationService reconciliationService;

  private Alert confirmation;

  public ResetPaymentCommand(PageController pageController,
      ReconciliationService reconciliationService) {
    this.pageController = pageController;
    this.reconciliationService = reconciliationService;
    setup();
  }

  private void setup() {
    ButtonType yes = new ButtonType("Definitief Wissen", ButtonData.YES);
    ButtonType no = new ButtonType("Annuleren", ButtonData.NO);
    confirmation = new Alert(AlertType.CONFIRMATION, "", yes, no);
    confirmation.setTitle("Betaalgegevens wissen");
    confirmation.setHeaderText("Betaalgegevens DEFINITIEF wissen");
    Stage cs = (Stage) confirmation.getDialogPane().getScene().getWindow();
    cs.getIcons().add(MainStage.getIcon());
  }

  @EventListener(menuChoice = MenuChoice.RESET_PAYMENTS)
  public void executeCommand() {
    Optional<ButtonType> result = confirmation.showAndWait();
    if (result.isPresent()) {
      ButtonType bt = result.get();
      if (ButtonData.YES == bt.getButtonData()) {
        reconciliationService.resetPaymentStatus();
        pageController.showMessage("Alle betaalgegevens zijn gewist");
      } else {
        pageController.showMessage("Geannuleerd");
      }
    }
  }

}
