package nl.ealse.ccnl.control.document;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.net.URL;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.util.WrappedFileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AddDocumentControllerTest extends FXMLBaseTest {

  private static WrappedFileChooser fileChooser;

  private AddDocumentController sut;

  @Test
  void testController() {
    final AtomicBoolean ar = new AtomicBoolean();
    runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      setFileChooser();
      doTest();
      ar.set(true);
    }, ar));
    
  }

  private void doTest() {
    Member m = member();
    MemberSeLectionEvent event = new MemberSeLectionEvent(sut, MenuChoice.ADD_DOCUMENT, m);
    sut.addDocument(event);

    sut.searchDocument();

    sut.addDocument();
    verify(getPageController()).showMessage("Document is toegevoegd");
  }

  private void prepare() {
    sut = getTestSubject(AddDocumentController.class);
    getPageWithFxController(sut, PageName.ADD_DOCUMENT);
  }

  @BeforeAll
  static void setup() {
    fileChooser = mock(WrappedFileChooser.class);
    URL url = DocumentController.class.getResource("/MachtigingsformulierSEPA.pdf");
    when(fileChooser.showOpenDialog()).thenReturn(new File(url.getFile()));
  }

  private void setFileChooser() {
    try {
      FieldUtils.writeField(sut, "fileChooser", fileChooser, true);
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
