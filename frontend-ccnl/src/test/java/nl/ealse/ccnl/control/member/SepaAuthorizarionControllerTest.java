package nl.ealse.ccnl.control.member;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.service.relation.MemberService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class SepaAuthorizarionControllerTest extends FXMLBaseTest<SepaAuthorizarionController> {

  private static PageController pageController;
  private static DocumentService documentService;
  private static MemberService service;
  private static WrappedFileChooser fileChooser;
  private static IbanController ibanController;


  private SepaAuthorizarionController sut;
  private Member m;

  @Test
  void testController() {
    sut = new SepaAuthorizarionController(pageController, documentService, service);
    setDirectory();
    m = member();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      setFileChooser();
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
    verify(pageController).showMessage("SEPA-machtiging opgeslagen bij lid");

    sut.printSepaPDF();
    sut.closePDFViewer();
  }

  private void prepare() {
    try {
      PageHelper ph = new PageHelper(pageController, ibanController);
      ph.configurePage();
      getPage(sut, PageName.SEPA_AUTHORIZATION_ADD);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
    pageController = mock(PageController.class);
    ibanController = mock(IbanController.class);
    documentService = mock(DocumentService.class);
    service= mock(MemberService.class);
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

  private void setDirectory() {
    try {
      FieldUtils.writeField(sut, "sepaDirectory", "C:/temp", true);
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
  
  private static class PageHelper extends FXMLBaseTest<IbanController> {
    private final PageController pageController;
    private final IbanController ibanController;
    
    private PageHelper(PageController pageController, IbanController ibanController) {
      this.pageController = pageController;
      this.ibanController = ibanController;
    }
    
    private void configurePage() {
      try {
        Parent d = getPage(ibanController, PageName.ADD_IBAN_NUMBER);
        when(pageController.loadPage(PageName.ADD_IBAN_NUMBER)).thenReturn(d);
      } catch (FXMLMissingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }
    
  }
}
