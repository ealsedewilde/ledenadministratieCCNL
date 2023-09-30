package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class SepaAuthorizarionControllerTest extends FXMLBaseTest<SepaAuthorizarionController> {

  private static WrappedFileChooser fileChooser;

  private SepaAuthorizarionController sut;
  private Member m;

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
    MemberSeLectionEvent event =
        new MemberSeLectionEvent(this, MenuChoice.PAYMENT_AUTHORIZATION, m);
    sut.onApplicationEvent(event);

    sut.addSepaPDF();
    verify(getPageController()).showMessage("SEPA-machtiging opgeslagen bij lid");

    sut.printSepaPDF();
    sut.closePDFViewer();
  }

  private void prepare() {
    sut = IbanTestHelper.getSepaAuthorizarionController(getPageController());
    doNothing().when(sut).closePDFViewer();
    setFile();
    setFileChooser();
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    Resource r = new ClassPathResource("MachtigingsformulierSEPA.pdf");
    try {
      File f = r.getFile();
      when(fileChooser.showOpenDialog()).thenReturn(f);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }


  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setMemberStatus(MembershipStatus.ACTIVE);
    m.setIbanNumber("GB33BUKB2020155555");
    return m;
  }


  private void setFile() {
    ClassPathResource r = new ClassPathResource("welkom.pdf");
    try {
      File f = r.getFile();
      FieldUtils.writeField(sut, "selectedFile", f, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
