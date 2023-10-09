package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.TextArea;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.MailService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


class CancelationMailControllerTest extends FXMLBaseTest {

  private PageController pageController;
  private DocumentService documentService;
  private MemberService memberService;
  private MailService mailService;

  private TextArea letterText;

  private CancelationMailController controller;

  @Test
  void doTest() {
   
    pageController = mock(PageController.class);
    documentService = mock(DocumentService.class);
    memberService = mock(MemberService.class);
    mailService = mock(MailService.class);

    final ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      controller =
          new CancelationMailController(pageController, documentService, mailService, memberService);
      prepare();
      controller.setup();
      setContent();
      Member m = new Member();
      m.setMemberNumber(4444);
      m.setInitials("tester");
      m.setEmail("test@ealse.nl");
      MemberSeLectionEvent event =
          new MemberSeLectionEvent(controller, MenuChoice.CANCEL_MEMBERSHIP, m);
      controller.onApplicationEvent(event);
      controller.sendMail();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
    verify(mailService).sendMail(anyString(), anyString(), arg.capture());
    Assertions.assertEquals("Beste tester\t\n Dit is een test.", arg.getValue());
  }

  private void prepare() {
    getPageWithoutFxController(controller, PageName.MEMBER_CANCEL_MAIL);
  }

  private void setContent() {
    letterText = controller.getLetterText();
    letterText.setText("Beste <<naam>>\n Dit is een test.");
    try {
      FieldUtils.writeDeclaredField(controller, "mailSubject", "test mail", true);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


}
