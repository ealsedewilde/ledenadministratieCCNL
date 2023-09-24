package nl.ealse.ccnl.control.club;

import java.util.Map;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalClubSearchControllerTest {

  private ExternalClubSearchController sut;

  @Test
  void testController() {
    sut = new ExternalClubSearchController(null, null, null);

    ExternalClubSelectionEvent result = sut.newEntitySelectionEvent(MenuChoice.AMEND_EXTERNAL_CLUB);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.getSearchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }

}
