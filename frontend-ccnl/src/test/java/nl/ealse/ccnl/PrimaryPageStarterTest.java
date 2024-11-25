package nl.ealse.ccnl;

import javafx.stage.Stage;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PrimaryPageStarterTest extends FXMLBaseTest {

  private PrimaryPageStarter sut;

  @Test
  void testSut() {
    Assertions.assertTrue(runFX(() -> {
      sut = new PrimaryPageStarter();
      config();
      doTest();
    }));


  }

  private void doTest() {
    Stage s = new Stage();
    StageReadyEvent event = new StageReadyEvent(s);
    sut.onApplicationEvent(event);
    Assertions.assertTrue(s.isShowing());
  }

  @BeforeAll
  static void setup() {}

  private void config() {
    try {
      FieldUtils.writeDeclaredField(sut, "applicationIcon", "Citroen.png", true);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
