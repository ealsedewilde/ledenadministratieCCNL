package nl.ealse.ccnl.control.club;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalClubDeleteControllerTest extends FXMLBaseTest {

  private ExternalClubDeleteController sut;
  private ExternalRelationClub club;

  @Test
  void testController() {
    sut = getTestSubject(ExternalClubDeleteController.class);
    club = club();

    Assertions.assertTrue(runFX(() -> {
      prepare();
      doTest();
      return Boolean.TRUE;
    }));
    
  }

  private void doTest() {
    ExternalClubSelectionEvent event =
        new ExternalClubSelectionEvent(sut, MenuChoice.DELETE_EXTERNAL_CLUB, club);
    sut.onApplicationEvent(event);

    sut.delete();
    verify(getPageController()).showMessage("Gegevens zijn verwijderd");
  }

  private void prepare() {
    reset(getPageController());
    getPageWithFxController(sut, PageName.EXTERNAL_CLUB_DELETE);
  }

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
