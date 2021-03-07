package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IbanControllerTest extends FXMLBaseTest<IbanController>{
  
  private static SepaAuthorizarionController parentController;
  
  private IbanController sut;
  private Member m;
  private TextField ibanNumber;
  private Label ibanNumberE;
  
  @Test
  void testController() {
    sut = new IbanController(parentController);
    m = member();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      initialize();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }
  
  private void doTest() {
    MemberSeLectionEvent event =
        new MemberSeLectionEvent(this, MenuChoice.PAYMENT_AUTHORIZATION, m);
    sut.onApplicationEvent(event);
    sut.save();
    Assertions.assertTrue(ibanNumberE.isVisible());
    Assertions.assertEquals("IBAN-nummer is verplicht", ibanNumberE.getText());
    
    ibanNumber.setText("GB33BUKB0000000000");
    sut.save();
    Assertions.assertTrue(ibanNumberE.isVisible());
    Assertions.assertEquals("IBAN-nummer onjuist", ibanNumberE.getText());
    
    ibanNumber.setText("GB33BUKB20201555555555");
    sut.save();
    Assertions.assertFalse(ibanNumberE.isVisible());
  }
  
  private void prepare() {
    when(parentController.getIbanNumberStage()).thenReturn(new Stage());
    try {
       getPage(sut, PageName.ADD_IBAN_NUMBER);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }
  
  @BeforeAll
  static void setup() {
    parentController = mock(SepaAuthorizarionController.class);
  }
  
  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setInitials("T");
    m.setLastName("Tester");
    m.setMemberStatus(MembershipStatus.ACTIVE);
    return m;
  }

  private void initialize() {
    try {
      Field f = sut.getClass().getDeclaredField("ibanNumber");
      f.setAccessible(true);
      ibanNumber = (TextField) f.get(sut);
      f = sut.getClass().getDeclaredField("ibanNumberE");
      f.setAccessible(true);
      ibanNumberE = (Label) f.get(sut);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
