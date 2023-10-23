package nl.ealse.ccnl.control.partner;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PartnerSearchControllerTest extends FXMLBaseTest {

  private PartnerSearchController sut;
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
    sut = new PartnerSearchController(null, null, null);
  }

  private void testSearch() {
    Assertions.assertEquals("Opzoeken adverteerder", sut.headerText(null));
    Assertions.assertEquals("Partner nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden adverteerders", sut.resultHeaderText(null));
    Platform.setImplicitExit(false);
  }

  private void testController() {
    PartnerSelectionEvent result = sut.newEntitySelectionEvent(MenuChoice.AMEND_PARTNER);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.getSearchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }


}
