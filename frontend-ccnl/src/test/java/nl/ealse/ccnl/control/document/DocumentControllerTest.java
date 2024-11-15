package nl.ealse.ccnl.control.document;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import nl.ealse.ccnl.control.DocumentViewer;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ioc.ComponentProviderUtil;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.service.DocumentService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DocumentControllerTest extends FXMLBaseTest {

  private static DocumentService documentService;
  private static MouseEvent mouseEvent;
  private static Document document;

  private DocumentController sut;

  @Test
  void testController() {
    AtomicBoolean ar = new AtomicBoolean();
    Assertions.assertTrue(runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar)));
    
  }

  private void doTest() {
    Member m = member();
    MemberSeLectionEvent event = new MemberSeLectionEvent(sut, MenuChoice.VIEW_DOCUMENT, m);
    sut.viewDocument(event);
    verify(getPageController()).setActivePage(PageName.VIEW_DOCUMENTS);
    sut.selectDocument(mouseEvent);
    sut.printDocument();
    sut.deleteDocument();
    verify(getPageController()).showMessage("Het document is verwijderd");
    sut.closeDocument();
  }


  private void prepare() {
    sut = getTestSubject(DocumentController.class);
    getPageWithFxController(sut, PageName.VIEW_DOCUMENTS);
    setPdfViewer();
    TableRow<Document> row = new TableRow<>();
    row.setItem(document());
    when(mouseEvent.getSource()).thenReturn(row);
  }


  private void setPdfViewer() {
    DocumentViewer documentViewer = DocumentViewer.builder().build();
    try {
      FieldUtils.writeField(sut, "documentViewer", documentViewer, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @BeforeAll
  static void setup() {
    documentService = ComponentProviderUtil.getComponent(DocumentService.class);
    document = document();
    List<Document> documents = new ArrayList<>();
    documents.add(document);
    when(documentService.findDocuments(any(Member.class))).thenReturn(documents);
    mouseEvent = mock(MouseEvent.class);
  }

  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    m.setInitials("T.");
    m.setLastNamePrefix("de");
    m.setLastName("Tester");
    List<Document> documents = new ArrayList<>();
    documents.add(document);
    m.setDocuments(documents);
    return m;
  }

  private static Document document() {
    Document d = new Document();
    d.setDocumentName("MachtigingsformulierSEPA.pdf");
    d.setPdf(getBlob("/MachtigingsformulierSEPA.pdf"));
    d.setOwner(member());
    return d;
  }

  private static byte[] getBlob(String name) {
    byte[] b = null;
    try (InputStream is = DocumentController.class.getResourceAsStream(name)) {
      b = is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return b;
  }



}
