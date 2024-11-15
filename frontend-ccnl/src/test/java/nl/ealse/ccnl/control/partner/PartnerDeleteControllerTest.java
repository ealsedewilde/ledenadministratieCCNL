package nl.ealse.ccnl.control.partner;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PartnerDeleteControllerTest extends FXMLBaseTest {

  private PartnerDeleteController sut;
  private ExternalRelationPartner partner;

  @Test
  void testController() {
    partner = externalRelationPartner();
    final AtomicBoolean ar = new AtomicBoolean();
    Assertions.assertTrue(runFX(new FutureTask<AtomicBoolean>(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar)));
    
  }

  private void doTest() {
    reset(getPageController());
    PartnerSelectionEvent event =
        new PartnerSelectionEvent(sut, MenuChoice.DELETE_PARTNER, partner);
    sut.onApplicationEvent(event);

    sut.delete();
    verify(getPageController()).showMessage("Gegevens zijn verwijderd");
  }

  private void prepare() {
    sut = getTestSubject(PartnerDeleteController.class);
    getPageWithFxController(sut, PageName.PARTNER_DELETE);
  }

  private ExternalRelationPartner externalRelationPartner() {
    ExternalRelationPartner r = new ExternalRelationPartner();
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
    r.setRelationName("De Partner");
    r.setModificationDate(LocalDate.of(2020, 12, 5));
    r.setContactName("Pietje Puk");
    r.setTelephoneNumber("06-01234567");
    r.setRelationNumber(8201);
    return r;
  }

}
