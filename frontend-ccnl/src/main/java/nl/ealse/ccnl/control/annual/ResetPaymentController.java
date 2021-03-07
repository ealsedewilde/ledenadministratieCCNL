package nl.ealse.ccnl.control.annual;

import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.ReconciliationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
@Lazy(false)
public class ResetPaymentController implements ApplicationListener<MenuChoiceEvent> {

  private final PageController pageController;
  
  private final ApplicationContext springContext;
 
  public ResetPaymentController(PageController pageController, ApplicationContext springContext) {
    this.pageController = pageController;
    this.springContext = springContext;
   }

  @Override
  public void onApplicationEvent(MenuChoiceEvent event) {
    if (MenuChoice.RESET_PAYMENTS == event.getMenuChoice()) {
      springContext.getBean(ReconciliationService.class).resetPaymentStatus();
      pageController.setMessage("Betaalgegevens zijn gewist");
    }
  }

}
