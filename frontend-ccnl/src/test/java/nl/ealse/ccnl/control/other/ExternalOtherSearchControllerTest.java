package nl.ealse.ccnl.control.other;

import java.util.Map;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalOtherSearchControllerTest {

  private ExternalOtherSearchController sut;

  @Test
  void testController() {
    sut = new ExternalOtherSearchController(null, null, null);
    ExternalOtherSelectionEvent result =
        sut.newEntitySelectionEvent(MenuChoice.AMEND_EXTERNAL_RELATION);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.getSearchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }


}
