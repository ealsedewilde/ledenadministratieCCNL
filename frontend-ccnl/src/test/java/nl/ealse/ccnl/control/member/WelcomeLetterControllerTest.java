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
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.ioc.ComponentProviderUtil;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.PrintCount;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class WelcomeLetterControllerTest extends FXMLBaseTest {

  @TempDir
  File tempDir;

  private static DocumentService documentService;
  private static WrappedFileChooser fileChooser;
  private static byte[] pdf;
  private static byte[] docx;

  private WelcomeLetterController controller;


  @Test
  void doTest() {

    final AtomicBoolean ar = new AtomicBoolean();
    runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      Member m = new Member();
      m.setMemberNumber(4444);
      m.setInitials("Pietje");
      m.setLastName("Puk");
      Address a = m.getAddress();
      a.setStreet("Straat");
      a.setAddressNumber("1");
      a.setPostalCode("1234 AA");
      a.setCity("Plaats");
      WelcomeletterEvent event = new WelcomeletterEvent(controller, m);
      controller.onApplicationEvent(event);
      controller.onApplicationEvent(event);
      controller.showLetterExample();
      controller.saveletter();

      controller.getAddSepa().setSelected(true);
      PrintCount.reset();
      controller.printLetter();
      controller.printPDF();
      Assertions.assertEquals(2, PrintCount.getCount());
      controller.closePDF();
      ar.set(true);
    }, ar));
    
    verify(documentService).generateWordDocument(any(LetterData.class));
    controller.getLetterText();
  }

  private void prepare() {
    controller = getTestSubject(WelcomeLetterController.class);
    getPageWithFxController(controller, PageName.WELCOME_LETTER);
    controller.getLetterText().setText("Beste <<naam>>, \n Welkom bij Citroën Club Nederland.");
    fileChooser = mock(WrappedFileChooser.class);
    File exportFile = new File(tempDir, "test.docx");
    when(fileChooser.showSaveDialog()).thenReturn(exportFile);
    setFileChooser();
  }

  @BeforeAll
  static void setup() {
    pdf = getBlob("/welkom.pdf");
    docx = getBlob("/welkom.docx");
    documentService = ComponentProviderUtil.getComponent(DocumentService.class);
    when(documentService.generatePDF(any(LetterData.class))).thenReturn(pdf);
    when(documentService.generateWordDocument(any(LetterData.class))).thenReturn(docx);
    List<Document> documents = new ArrayList<>();
    when(documentService.findDocuments(any(Member.class), eq(DocumentType.WELCOME_LETTER)))
        .thenReturn(documents);
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(controller, "fileChooser", fileChooser, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  protected void getPDF() {}

  private static byte[] getBlob(String name) {
    byte[] b = null;
    try (InputStream is = WelcomeLetterController.class.getResourceAsStream(name)) {
      b = is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return b;
  }


}
