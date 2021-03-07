package nl.ealse.ccnl.control.partner;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PartnerSearchTest extends FXBase {

  private PartnerSearch sut;

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
    sut = new PartnerSearch();
    Assertions.assertEquals("Opzoeken adverteerder", sut.headerText(null));
    Assertions.assertEquals("Partner nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden adverteerders", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }

}
