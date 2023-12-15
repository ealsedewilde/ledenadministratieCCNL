package nl.ealse.ccnl.control.club;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.service.relation.ExternalClubService;
import nl.ealse.ccnl.service.relation.SearchItem;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExternalClubSearchControllerTest extends FXMLBaseTest {

  private ExternalClubSearchController sut; 

  
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
    sut = ExternalClubSearchController.getInstance();
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

  @BeforeAll
  static void setup() {
    MockProvider.mock(ExternalClubService.class);
  };

}
