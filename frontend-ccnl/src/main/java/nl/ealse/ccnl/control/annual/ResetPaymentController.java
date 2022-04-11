package nl.ealse.ccnl.control.annual;

import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.ReconciliationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false)
public class ResetPaymentController {

  private final PageController pageController;

  private final ApplicationContext springContext;

  public ResetPaymentController(PageController pageController, ApplicationContext springContext) {
    this.pageController = pageController;
    this.springContext = springContext;
  }

  @EventListener(condition = "#event.name('RESET_PAYMENTS')")
  public void onApplicationEvent(MenuChoiceEvent event) {
    springContext.getBean(ReconciliationService.class).resetPaymentStatus();
    pageController.showMessage("Betaalgegevens zijn gewist");
  }

}
