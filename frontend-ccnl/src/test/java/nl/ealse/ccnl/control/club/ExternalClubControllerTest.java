package nl.ealse.ccnl.control.club;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.service.relation.ExternalClubService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExternalClubControllerTest extends FXMLBaseTest<ExternalClubController> {

  private static ExternalClubService service;

  private ExternalClubController sut;
  private ExternalRelationClub club;

  @Test
  void testController() {
    sut = new ExternalClubController(getPageController(), service);
    club = club();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
     ExternalClubSelectionEvent event =
        new ExternalClubSelectionEvent(sut, MenuChoice.NEW_EXTERNAL_CLUB, club);
    sut.handleClub(event);

    sut.save();
    verify(getPageController()).showMessage("Club gegevens zijn opgeslagen");

    sut.nextPage();
    sut.previousPage();
  }

  @BeforeAll
  static void setup() {
    service = mock(ExternalClubService.class);
  };

  private ExternalRelationClub club() {
    ExternalRelationClub r = new ExternalRelationClub();
    Address a = r.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setAddressNumberAppendix("B");
    a.setCity("Ons Dorp");
    a.setPostalCode("1234 AA");

    r.setEmail("info@club.com");
    r.setContactNamePrefix("t.n.v.");
    r.setRelationInfo("Some info");
    r.setRelationSince(LocalDate.of(1998, 12, 5));
    r.setRelationName("Het Clubje");
    r.setModificationDate(LocalDate.of(2020, 12, 5));
    r.setContactName("Pietje Puk");
    r.setTelephoneNumber("06-01234567");
    r.setRelationNumber(8201);
    return r;
  }

}
