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
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class MemberControllerTest extends FXMLBaseTest {

  private static DocumentService documentService;
  private static MemberService service;
  private static ApplicationEventPublisher eventPublisher;

  private MemberController controller;
  private FormController formController;
  private Member m;

  @Test
  void testController() {

    service = mock(MemberService.class);
    documentService = mock(DocumentService.class);
    final AtomicBoolean ar = new AtomicBoolean();
    m = getMember();
    AtomicBoolean result = runFX(() -> {
      controller =
          new MemberController(getPageController(), service, documentService, eventPublisher);
      controller.setup();
      formController = getFormController();
      prepare();
      doTest();
      testFormController();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());

  }

  void testFormController() {
    formController = getFormController();
    Parent[] formPanes = getFormPanes();
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
    formController.nextPage();
    formController.nextPage();
    formController.nextPage();
    formController.previousPage();
    formController.previousPage();
    formController.previousPage();
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
  @BeforeAll
  static void setup() {
    eventPublisher = mock(ApplicationEventPublisher.class);
    service = mock(MemberService.class);
    documentService = mock(DocumentService.class);
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

  private FormController getFormController() {
    try {
      return (FormController) FieldUtils.readField(controller, "formController", true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Parent[] getFormPanes() {
    try {
      return (Parent[]) FieldUtils.readField(formController, "formPageArray", true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }



}
