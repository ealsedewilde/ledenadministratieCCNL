package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IbanControllerTest extends FXMLBaseTest {
  private static WrappedFileChooser fileChooser;

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
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    AddIbanNumberEvent event = new AddIbanNumberEvent(m, this::nextAction);
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
    sut = getTestSubject(IbanController.class);
    setFileChooser();
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    when(fileChooser.showSaveDialog()).thenReturn(new File("welkom.pdf"));
  }


  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setInitials("T");
    m.setLastName("Tester");
    m.setMemberStatus(MembershipStatus.ACTIVE);
    return m;
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(getTestSubject(SepaAuthorizarionController.class), "fileChooser",
          fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initialize() {
    try {
      ibanNumber = (TextField) FieldUtils.readDeclaredField(sut, "ibanNumber", true);
      ibanNumberE = (Label) FieldUtils.readDeclaredField(sut, "ibanNumberE", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Void nextAction() {
    return null;
  }


}
