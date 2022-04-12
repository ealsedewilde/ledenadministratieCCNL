package nl.ealse.ccnl.control.annual;

import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.service.ReconciliationService;
import org.springframework.stereotype.Controller;

@Controller
public class ResetPaymentCommand {

  private final PageController pageController;

  private final ReconciliationService reconciliationService;

  public ResetPaymentCommand(PageController pageController, ReconciliationService reconciliationService) {
    this.pageController = pageController;
    this.reconciliationService = reconciliationService;
  }

  public void executeCommand() {
    reconciliationService.resetPaymentStatus();
    pageController.showMessage("Betaalgegevens zijn gewist");
  }

}
