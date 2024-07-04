package nl.ealse.ccnl.ioc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Test;


class ControllerProviderTest extends FXBase {

  @Test
  void TestGetController() {
    ComponentProvider provider = new DefaultComponentProvider();
    PageController controller = provider.getComponent(PageController.class);
    assertNotNull(controller);
  }

}
