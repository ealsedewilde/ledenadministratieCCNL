package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class MemberControllerTest extends FXMLBaseTest<MemberController> {

  private PageController pageController;
  private DocumentService documentService;
  private MemberService service;

  private MemberController controller;
  private Member m;

  @Test
  void testController() {
   
    pageController = mock(PageController.class);
    service = mock(MemberService.class);
    documentService = mock(DocumentService.class);
    controller =
        new MemberController(pageController, service, documentService);
    final AtomicBoolean ar = new AtomicBoolean();
    m = getMember();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

  }

  private void doTest() {
    Document document = new Document();
    byte[] pdf = getBlob("MachtigingsformulierSEPA.pdf");
    document.setPdf(pdf);
    document.setOwner(m);
    when(documentService.findSepaAuthorization(m)).thenReturn(Optional.of(document));
    MemberSeLectionEvent event = new MemberSeLectionEvent(controller, MenuChoice.AMEND_MEMBER, m);
    controller.amendMember(event);
    fillIbanNumber();
    controller.nextPage();
    controller.nextPage();
    controller.nextPage();
    controller.previousPage();
    controller.previousPage();
    controller.previousPage();
    controller.save();
    verify(service).persistMember(any(Member.class));

    controller.showSepaAuthorization();
    controller.printPDF();
    controller.deletePDF();
    controller.closePDF();
    event = new MemberSeLectionEvent(controller, MenuChoice.NEW_MEMBER, m);
    controller.newMember(event);

  }

  private void prepare() {
    try {
      Parent p = getPage(controller, PageName.MEMBER_PERSONAL);
      when(pageController.loadPage(PageName.MEMBER_PERSONAL)).thenReturn(p);
      Parent a = getPage(controller, PageName.MEMBER_ADDRESS);
      when(pageController.loadPage(PageName.MEMBER_ADDRESS)).thenReturn(a);
      Parent f = getPage(controller, PageName.MEMBER_FINANCIAL);
      when(pageController.loadPage(PageName.MEMBER_FINANCIAL)).thenReturn(f);
      Parent e = getPage(controller, PageName.MEMBER_EXTRA);
      when(pageController.loadPage(PageName.MEMBER_EXTRA)).thenReturn(e);

      Parent s = getPage(controller, PageName.SEPA_AUTHORIZATION_SHOW);
      when(pageController.loadPage(PageName.SEPA_AUTHORIZATION_SHOW)).thenReturn(s);

      when(documentService.findSepaAuthorization(m)).thenReturn(Optional.empty());
      Assertions.assertNotNull(p);
    } catch (FXMLMissingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private Member getMember() {
    Member m = new Member();
    //m.setInitials("T.");
    //m.setLastName("Tester");
    Address a = m.getAddress();
    a.setStreet("Straat");
    a.setAddressNumber("1");
    a.setPostalCode("1234 AA");
    a.setCity("Plaats");
    a.setAddressInvalid(true);
    m.setMemberNumber(4999);
    return m;
  }


  protected byte[] getBlob(String name) {
    byte[] b = null;
    Resource r = new ClassPathResource(name);
    try (InputStream is = r.getInputStream()) {
      b = is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return b;
  }


  private void fillIbanNumber() {
    controller.getInitials().setText("T.");
    controller.getLastNamePrefix().setText("de");
    controller.getLastName().setText("Tester");
    controller.getIbanNumber().setText("foo");
  }


}
