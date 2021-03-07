package nl.ealse.ccnl.control.club;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalClubSearchTest extends FXBase {

  private ExternalClubSearch sut;
  
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
    sut = new ExternalClubSearch();
    Assertions.assertEquals("Opzoeken club", sut.headerText(null));
    Assertions.assertEquals("Club nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden Clubs", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }

}
