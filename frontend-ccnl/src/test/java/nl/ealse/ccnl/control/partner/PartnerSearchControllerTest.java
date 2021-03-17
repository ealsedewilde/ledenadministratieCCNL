package nl.ealse.ccnl.control.partner;

import java.util.Map;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PartnerSearchControllerTest {

  private PartnerSearchController sut;

  @Test
  void testController() {
    sut = new PartnerSearchController(null, null);
    PartnerSelectionEvent result = sut.newEntitySelectionEvent(MenuChoice.AMEND_PARTNER);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.searchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }


}
