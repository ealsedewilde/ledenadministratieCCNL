package nl.ealse.ccnl.control;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.stage.Stage;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.test.FXBase;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class PDFViewerTest extends FXBase {
  
  private PDFViewer sut;
  private Stage pdfStage;
  
  @Test
  void testViewer() {
    AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }
  
  private void doTest() {
    sut.showPDF( getPdf("welkom.pdf"), member());
    assertTrue(pdfStage.isShowing());
    sut.close();
    assertTrue(!pdfStage.isShowing());
  }
  
  private void prepare() {
    sut = PDFViewer.builder().build();
    sut.setWindowTitle("Welkomsbrief voor lid: %d (%s)");
    pdfStage = getStage();
  }
  
  private byte[] getPdf(String name) {
    byte[] b = null;
    Resource r = new ClassPathResource(name);
    try (InputStream is = r.getInputStream()) {
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
      return (Stage) FieldUtils.readDeclaredField(sut, "pdfStage", true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }



}
