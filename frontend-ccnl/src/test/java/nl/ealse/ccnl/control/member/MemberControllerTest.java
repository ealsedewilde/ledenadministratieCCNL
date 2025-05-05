package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ioc.ComponentProvider;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberControllerTest extends FXMLBaseTest {

  private static DocumentService documentService;
  private static MemberService service;

  private MemberController controller;
  private FormController formController;
  private Member m;

  @Test
  void testController() {
    Assertions.assertTrue(runFX(() -> {
      prepare();
      doTest();
      testFormController();
    }));

  }

  void testFormController() {
    formController = getFormController();
    Parent[] formPanes = getFormPanes();
    Assertions.assertNotNull(formPanes[3]);
  }

  private void doTest() {
    Document document = new Document();
    byte[] pdf = getBlob("/MachtigingsformulierSEPA.pdf");
    document.setDocumentName("MachtigingsformulierSEPA.pdf");
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
    verify(service).save(any(Member.class));

    controller.showSepaAuthorization();
    controller.printPDF();
    controller.deletePDF();
    controller.closePDF();
    event = new MemberSeLectionEvent(controller, MenuChoice.NEW_MEMBER, m);
    controller.newMember(event);
    
    amountPaidTest();

  }

  private void prepare() {
    m = getMember();
    service = ComponentProvider.getComponent(MemberService.class);
    reset(service);
    documentService = ComponentProvider.getComponent(DocumentService.class);
    when(documentService.findSepaAuthorization(m)).thenReturn(Optional.empty());
    controller = getTestSubject(MemberController.class);
    formController = getFormController();
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
    try (InputStream is = MemberController.class.getResourceAsStream(name)) {
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
  
  private void amountPaidTest() {
    controller.getAmountPaid().setVisible(true);
    controller.setAmountPaid(true);
    String result = controller.getAmountPaid().getText();
    controller.setAmountPaid(false);
    Assertions.assertEquals("35,00", result);
    result = controller.getAmountPaid().getText();
    Assertions.assertEquals("0,00", result);
    
    controller.amountPaidVisibility("overboeking");
    Assertions.assertTrue(controller.getAmountPaid().isVisible());
    controller.amountPaidVisibility("automatische incasso");
    Assertions.assertFalse(controller.getAmountPaid().isVisible());
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
