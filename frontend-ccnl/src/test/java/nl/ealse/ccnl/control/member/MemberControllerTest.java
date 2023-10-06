package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.form.FormPane;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class MemberControllerTest extends FXMLBaseTest<MemberController> {

  private DocumentService documentService;
  private MemberService service;

  private MemberController controller;
  private TestFormPages formPages;
  private Member m;

  @Test
  void testController() {

    service = mock(MemberService.class);
    documentService = mock(DocumentService.class);
    final AtomicBoolean ar = new AtomicBoolean();
    m = getMember();
    AtomicBoolean result = runFX(() -> {
      controller = new MemberController(getPageController(), service, documentService);
      prepare();
      doTest();
      testFormPages();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

  }
  
   void testFormPages() {
   formPages = new TestFormPages(controller);
    FormPane[] formPanes = formPages.getFormPages();
    Assertions.assertNotNull(formPanes[3]);
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
    when(documentService.findSepaAuthorization(m)).thenReturn(Optional.empty());
  }

  private Member getMember() {
    Member m = new Member();
    // m.setInitials("T.");
    // m.setLastName("Tester");
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
  
  
  private static class TestFormPages extends MemberFormPages {

    public TestFormPages(MemberController controller) {
      super(controller);
    }
    
    public  FormPane[] getFormPages() {
      return formPageArray;
    }
    
  }



}
