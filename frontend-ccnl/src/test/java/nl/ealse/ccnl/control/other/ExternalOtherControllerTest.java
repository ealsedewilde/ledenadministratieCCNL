package nl.ealse.ccnl.control.other;

import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalOtherControllerTest extends FXMLBaseTest {

  private ExternalOtherController sut;
  private ExternalRelationOther relation;

  @Test
  void testController() {
    relation = externalRelationOther();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      sut = getTestSubject(ExternalOtherController.class);
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    ExternalOtherSelectionEvent event =
        new ExternalOtherSelectionEvent(sut, MenuChoice.AMEND_EXTERNAL_RELATION, relation);
    sut.amendRelation(event);

    sut.save();
    verify(getPageController()).showMessage("Externe relatie opgeslagen");

    sut.getFormController().nextPage();
    sut.getFormController().previousPage();
  }

  private ExternalRelationOther externalRelationOther() {
    ExternalRelationOther r = new ExternalRelationOther();
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
    r.setRelationName("Een Relatie");
    r.setModificationDate(LocalDate.of(2020, 12, 5));
    r.setContactName("Pietje Puk");
    r.setTelephoneNumber("06-01234567");
    r.setRelationNumber(8201);
    return r;
  }

}
