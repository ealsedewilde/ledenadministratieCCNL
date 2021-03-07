package nl.ealse.ccnl.control.other;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalOtherSearchTest extends FXBase {

  private ExternalOtherSearch sut;
  
  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      testSearch();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void testSearch() {
    sut = new ExternalOtherSearch();
    Assertions.assertEquals("Opzoeken externe relatie", sut.headerText(null));
    Assertions.assertEquals("Relatie nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden externe relaties", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }


}
