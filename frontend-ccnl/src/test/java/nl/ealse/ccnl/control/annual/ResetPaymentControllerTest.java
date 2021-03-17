package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.service.ReconciliationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class ResetPaymentControllerTest {

  private static ApplicationContext springContext;
  private static ReconciliationService service;
  private static PageController pageController;

  @Test
  void testController() {
    ResetPaymentController sut = new ResetPaymentController(pageController, springContext);
    MenuChoiceEvent event = new MenuChoiceEvent(sut, MenuChoice.RESET_PAYMENTS);
    sut.onApplicationEvent(event);
    verify(pageController).showMessage("Betaalgegevens zijn gewist");
  }

  @BeforeAll
  static void setup() {
    springContext = mock(ApplicationContext.class);
    pageController = mock(PageController.class);
    service = mock(ReconciliationService.class);
    when(springContext.getBean(ReconciliationService.class)).thenReturn(service);
  }

}
