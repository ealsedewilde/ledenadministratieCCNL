package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
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
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


class CancelationMailControllerTest extends FXMLBaseTest<CancelationMailController> {

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
    try {
      Parent h = getPageWithFxController(controller, PageName.MAIL_HELP);
      when(pageController.loadPage(PageName.MAIL_HELP)).thenReturn(h);
      getPageWithoutFxController(controller, PageName.MEMBER_CANCEL_MAIL);
    } catch (FXMLMissingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void setContent() {
    letterText = controller.getLetterText();
    letterText.setText("Beste <<naam>>\n Dit is een test.");
    try {
      Field f = CancelationMailController.class.getDeclaredField("mailSubject");
      f.setAccessible(true);
      f.set(controller, "test mail");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


}
