package nl.ealse.ccnl.control;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.stage.Stage;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.test.FXBase;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentViewerTest extends FXBase {

  private DocumentViewer sut;
  private Stage pdfStage;

  @Test
  void testViewer() {
    AtomicBoolean ar = new AtomicBoolean();
    Assertions.assertTrue(runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar)));
    
  }

  private void doTest() {
    Member m = member();
    sut.showPdf(getPdf("/welkom.pdf"), m);
    pdfStage = getStage();
    assertTrue(pdfStage.isShowing());
    sut.close();
    assertTrue(!pdfStage.isShowing());
    Document d = new Document();
    d.setDocumentName("Citroen.png");
    d.setPdf(getPdf("/Citroen.png"));
    d.setOwner(m);
    sut.showDocument(d);
  }

  private void prepare() {
    sut = DocumentViewer.builder().build();
    sut.setWindowTitle("Welkomsbrief voor lid: %d (%s)");
  }

  private byte[] getPdf(String name) {
    byte[] b = null;
    try (InputStream is = getClass().getResourceAsStream(name)) {
      b = is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return b;
  }

  private Member member() {
    Member m = new Member();
    m.setMemberNumber(920);
    m.setInitials("T.");
    m.setLastName("Tester");
    return m;
  }

  private Stage getStage() {
    try {
      return (Stage) FieldUtils.readDeclaredField(sut, "documentViewerStage", true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }



}
