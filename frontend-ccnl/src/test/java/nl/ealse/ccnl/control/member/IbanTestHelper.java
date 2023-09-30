package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import org.apache.commons.lang3.reflect.FieldUtils;

public class IbanTestHelper {
  
  public static SepaAuthorizarionController getSepaAuthorizarionController(PageController pageController) {
      SepaAuthorizarionController controller = spy(new SepaAuthorizarionController(pageController, 
          mock(DocumentService.class), mock(MemberService.class)));
      doNothing().when(controller).selectSepaAuthorization();
      setDirectory(controller);
      controller.setup();
      return controller;
  }
  

  private static void setDirectory(SepaAuthorizarionController controller) {
    try {
      FieldUtils.writeField(controller, "sepaDirectory", "C:/temp", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
