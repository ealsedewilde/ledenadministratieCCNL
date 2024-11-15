package nl.ealse.ccnl.control.other;

import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalOtherSearchControllerTest extends FXMLBaseTest {

  private ExternalOtherSearchController sut;

  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    Assertions.assertTrue(runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      testController();
      testSearch();
      ar.set(true);
    }, ar)));
    
  }

  private void prepare() {
    sut = getTestSubject(ExternalOtherSearchController.class);
  }

  private void testSearch() {
    Assertions.assertEquals("Opzoeken externe relatie", sut.headerText(null));
    Assertions.assertEquals("Relatie nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden externe relaties", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }

  private void testController() {
    ExternalOtherSelectionEvent result =
        sut.newEntitySelectionEvent(MenuChoice.AMEND_EXTERNAL_RELATION);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.getSearchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }

}
