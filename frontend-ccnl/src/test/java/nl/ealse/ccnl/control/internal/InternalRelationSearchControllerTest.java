package nl.ealse.ccnl.control.internal;

import java.util.Map;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InternalRelationSearchControllerTest {

  private InternalRelationSearchController sut;

  @Test
  void testController() {
    sut = new InternalRelationSearchController(null, null);

    InternalRelationSelectionEvent result =
        sut.newEntitySelectionEvent(MenuChoice.AMEND_INTERNAL_RELATION);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.searchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }


}
