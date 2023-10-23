package nl.ealse.ccnl.control.internal;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InternalRelationSearchControllerTest extends FXMLBaseTest{

  private InternalRelationSearchController sut;

  @Test
  void performTests() {
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      testController();
      testSearch();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }
  
  private void prepare() {
    sut = new InternalRelationSearchController(null, null, null);
  }
  
  private void testController() {
 
    InternalRelationSelectionEvent result =
        sut.newEntitySelectionEvent(MenuChoice.AMEND_INTERNAL_RELATION);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.getSearchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }

  private void testSearch() {
    Assertions.assertEquals("Opzoeken functie", sut.headerText(null));
    Assertions.assertEquals("Intern nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden functies", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }


}
