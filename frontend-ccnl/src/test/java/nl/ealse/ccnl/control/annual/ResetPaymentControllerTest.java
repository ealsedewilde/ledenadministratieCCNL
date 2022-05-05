package nl.ealse.ccnl.control.annual;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.service.ReconciliationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ResetPaymentControllerTest {

  private static ReconciliationService service;
  private static PageController pageController;

  @Test
  void testController() {
    ResetPaymentCommand sut = new ResetPaymentCommand(pageController, service);
    sut.executeCommand(null);
    verify(pageController).showMessage("Betaalgegevens zijn gewist");
  }

  @BeforeAll
  static void setup() {
    pageController = mock(PageController.class);
    service = mock(ReconciliationService.class);
  }

}
