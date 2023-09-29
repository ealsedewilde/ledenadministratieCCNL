package nl.ealse.ccnl.control.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.PrintCount;
import nl.ealse.javafx.FXMLMissingException;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class WelcomeLetterControllerTest extends FXMLBaseTest<WelcomeLetterController> {

  @TempDir
  File tempDir;

  private PageController pageController;
  private DocumentService documentService;
  private WrappedFileChooser fileChooser;

  private WelcomeLetterController controller;

  private byte[] pdf;

  private byte[] docx;

  @Test
  void doTest() {
   
    pageController = mock(PageController.class);

    documentService = mock(DocumentService.class);
    when(documentService.generatePDF(any(LetterData.class))).thenReturn(pdf);
    when(documentService.generateWordDocument(any(LetterData.class))).thenReturn(docx);
    List<Document> documents = new ArrayList<>();
    when(documentService.findDocuments(any(Member.class), eq(DocumentType.WELCOME_LETTER)))
        .thenReturn(documents);

    fileChooser = mock(WrappedFileChooser.class);
    File exportFile = new File(tempDir, "test.docx");
    when(fileChooser.showSaveDialog()).thenReturn(exportFile);


    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      controller = new WelcomeLetterController(pageController, documentService);
      prepare();
      controller.setup();
      controller.getLetterText().setText("Beste <<naam>>, \n Welkom bij Citroën Club Nederland.");

      Member m = new Member();
      m.setMemberNumber(4444);
      m.setInitials("Pietje");
      m.setLastName("Puk");
      Address a = m.getAddress();
      a.setStreet("Straat");
      a.setAddressNumber("1");
      a.setPostalCode("1234 AA");
      a.setCity("Plaats");
      MemberSeLectionEvent event = new MemberSeLectionEvent(controller, MenuChoice.NEW_MEMBER, m);
      controller.onApplicationEvent(event);
      controller.showLetterExample();
      setFileChooser();
      controller.saveletter();

      controller.getAddSepa().setSelected(true);
      PrintCount.reset();
      controller.printLetter();
      controller.printPDF();
      Assertions.assertEquals(2, PrintCount.getCount());
      controller.closePDF();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
    verify(documentService).generateWordDocument(any(LetterData.class));
    controller.getLetterText();
  }

  private void prepare() {
    try {
      Parent h = getPageWithFxController(controller, PageName.WELCOME_TEXT_HELP);
      when(pageController.loadPage(PageName.WELCOME_TEXT_HELP)).thenReturn(h);
      Parent s = getPageWithoutFxController(controller, PageName.WELCOME_LETTER_SHOW);
      when(pageController.loadPage(PageName.WELCOME_LETTER_SHOW)).thenReturn(s);
      getPageWithoutFxController(controller, PageName.WELCOME_LETTER);
    } catch (FXMLMissingException e) {
      e.printStackTrace();
    }
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(controller, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  protected void getPDF() {
    pdf = getBlob("welkom.pdf");
    docx = getBlob("welkom.docx");
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


}
