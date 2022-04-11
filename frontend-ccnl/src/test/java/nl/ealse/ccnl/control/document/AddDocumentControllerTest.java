package nl.ealse.ccnl.control.document;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class AddDocumentControllerTest extends FXMLBaseTest<AddDocumentController> {

  private static PageController pageController;
  private static DocumentService documentService;
  private static WrappedFileChooser fileChooser;

  private AddDocumentController sut;

  @Test
  void testController() {
    sut = new AddDocumentController(pageController, documentService);
    sepaDirectory();
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
    Member m = member();
    MemberSeLectionEvent event = new MemberSeLectionEvent(sut, MenuChoice.ADD_DOCUMENT, m);
    sut.addDocument(event);

    sut.searchDocument();

    sut.addDocument();
    verify(pageController).showMessage("Document is toegevoegd");
  }

  private void prepare() {
    try {
      getPage(sut, PageName.ADD_DOCUMENT);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    documentService = mock(DocumentService.class);
    fileChooser = mock(WrappedFileChooser.class);
    Resource r = new ClassPathResource("MachtigingsformulierSEPA.pdf");
    try {
      when(fileChooser.showOpenDialog()).thenReturn(r.getFile());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sepaDirectory() {
    try {
      FieldUtils.writeField(sut, "sepaDirectory", "C:/temp", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setInitials("T.");
    m.setLastNamePrefix("de");
    m.setLastName("Tester");
    return m;
  }

}
