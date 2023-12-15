package nl.ealse.ccnl.control.partner;

import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.CommercialPartnerService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PartnerControllerTest extends FXMLBaseTest {

  private static PartnerController sut;
  private ExternalRelationPartner partner;
 
  @Test
  void testController() {
    partner = externalRelationPartner();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      sut = PartnerController.getInstance();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    PartnerSelectionEvent event = new PartnerSelectionEvent(sut, MenuChoice.AMEND_PARTNER, partner);
    sut.amendPartner(event);

    sut.save();
    verify(getPageController()).showMessage("Partnergegevens opgeslagen");

    sut.getFormController().nextPage();
    sut.getFormController().previousPage();
  }

  @BeforeAll
  static void setup() {
     MockProvider.mock(CommercialPartnerService.class);
  };

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
