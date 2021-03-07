package nl.ealse.ccnl.control.internal;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.test.FXBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InternalRelationSearchTest extends FXBase {

  
  private InternalRelationSearch sut;
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
    sut = new InternalRelationSearch();
    Assertions.assertEquals("Opzoeken functie", sut.headerText(null));
    Assertions.assertEquals("Intern nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden functies", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }

}
