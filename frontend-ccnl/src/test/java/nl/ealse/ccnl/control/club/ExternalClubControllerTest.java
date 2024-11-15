package nl.ealse.ccnl.control.club;

import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalClubControllerTest extends FXMLBaseTest {

  private ExternalClubController sut;
  private ExternalRelationClub club;

  @Test
  void testController() {
    club = club();
    final AtomicBoolean ar = new AtomicBoolean();
    Assertions.assertTrue(runFX(new FutureTask<AtomicBoolean>(() -> {
      sut = getTestSubject(ExternalClubController.class);
      doTest();
      ar.set(true);
    }, ar)));
    
  }

  private void doTest() {
    ExternalClubSelectionEvent event =
        new ExternalClubSelectionEvent(sut, MenuChoice.AMEND_EXTERNAL_CLUB, club);
    sut.amendClub(event);

    sut.save();
    verify(getPageController()).showMessage("Club gegevens zijn opgeslagen");

    sut.getFormController().nextPage();
    sut.getFormController().previousPage();
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
