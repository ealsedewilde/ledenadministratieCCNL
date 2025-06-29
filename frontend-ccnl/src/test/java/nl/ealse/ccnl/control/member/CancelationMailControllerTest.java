package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import jakarta.mail.MessagingException;
import javafx.scene.control.TextArea;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.MailService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


class CancelationMailControllerTest extends FXMLBaseTest {

  private static MailService mailService;

  private CancelationMailController controller;

  @Test
  void doTest() throws MessagingException {
    final ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
    Assertions.assertTrue(runFX(() -> {
      Member m = new Member();
      m.setMemberNumber(4444);
      m.setInitials("tester");
      m.setEmail("test@ealse.nl");
      prepare();
      CancelMailEvent event = new CancelMailEvent(controller, m);
      controller.onApplicationEvent(event);
      setContent();
      controller.sendMail();
    }));

    verify(mailService).sendMail(anyString(), anyString(), arg.capture());
    Assertions.assertEquals("Beste tester\t\n Dit is een test.", arg.getValue());
  }

  private void prepare() {
    controller = getTestSubject(CancelationMailController.class);
    getPageWithFxController(controller, PageName.MEMBER_CANCEL_MAIL);
  }

  @BeforeAll
  static void setup() {
    mailService = ApplicationContext.getComponent(MailService.class);
  }

  private void setContent() {
    TextArea letterText = controller.getLetterText();
    letterText.setText("Beste <<naam>>\n Dit is een test.");
  }


}
