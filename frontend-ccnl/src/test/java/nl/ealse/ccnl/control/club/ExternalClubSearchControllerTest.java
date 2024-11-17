package nl.ealse.ccnl.control.club;

import java.util.Map;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalClubSearchControllerTest extends FXMLBaseTest {

  private ExternalClubSearchController sut;


  @Test
  void performTests() {

    Assertions.assertTrue(runFX(() -> {
      prepare();
      testController();
      testSearch();
      return Boolean.TRUE;
    }));
    
  }

  private void prepare() {
    sut = getTestSubject(ExternalClubSearchController.class);
  }

  private void testController() {
    ExternalClubSelectionEvent result = sut.newEntitySelectionEvent(MenuChoice.AMEND_EXTERNAL_CLUB);
    Assertions.assertNotNull(result);

    Map<String, SearchItem> itemMap = sut.getSearchItemValues();
    Assertions.assertEquals(5, itemMap.size());
  }

  private void testSearch() {
    Assertions.assertEquals("Opzoeken club", sut.headerText(null));
    Assertions.assertEquals("Club nr.", sut.columnName(0));
    Assertions.assertEquals("Gevonden Clubs", sut.resultHeaderText(null));
  }

}
