package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.mock;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

class IbanControllerTest extends FXMLBaseTest<IbanController>{
  
  private static ApplicationEventPublisher eventPublisher;
  
  private IbanController sut;
  private Member m;
  private TextField ibanNumber;
  private Label ibanNumberE;
  
  @Test
  void testController() {
    m = member();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());  }
  
  private void doTest() {
    MemberSeLectionEvent event =
        new MemberSeLectionEvent(this, MenuChoice.PAYMENT_AUTHORIZATION, m);
    sut.onApplicationEvent(event);
    initialize();

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
    sut = new IbanController(eventPublisher, getPageController());
    sut.setup();
  }
  
  @BeforeAll
  static void setup() {
    eventPublisher = mock(ApplicationEventPublisher.class);
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
      ibanNumber = (TextField) FieldUtils.readDeclaredField(sut, "ibanNumber", true);
      ibanNumberE = (Label) FieldUtils.readDeclaredField(sut, "ibanNumberE", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

}
