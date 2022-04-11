package nl.ealse.ccnl.control.partner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Parent;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.service.relation.CommercialPartnerService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PartnerControllerTest extends FXMLBaseTest<PartnerController> {

  private static PageController pageController;
  private static CommercialPartnerService service;

  private PartnerController sut;
  private ExternalRelationPartner partner;

  @Test
  void testController() {
    sut = new PartnerController(pageController, service);
    partner = externalRelationPartner();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    PartnerSelectionEvent event = new PartnerSelectionEvent(sut, MenuChoice.NEW_PARTNER, partner);
    sut.handlePartner(event);

    sut.save();
    verify(pageController).showMessage("Partnergegevens opgeslagen");

    sut.nextPage();
    sut.previousPage();
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    service = mock(CommercialPartnerService.class);
  };

  private void prepare() {
    try {
      getPage(sut, PageName.PARTNER_PERSONAL);
      Parent p = getPage(sut, PageName.PARTNER_ADDRESS);
      when(pageController.loadPage(PageName.PARTNER_ADDRESS)).thenReturn(p);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
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
