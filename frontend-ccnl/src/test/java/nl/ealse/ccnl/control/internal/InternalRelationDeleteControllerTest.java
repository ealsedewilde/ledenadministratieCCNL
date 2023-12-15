package nl.ealse.ccnl.control.internal;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InternalRelationDeleteControllerTest extends FXMLBaseTest {

  private InternalRelationDeleteController sut;
  private InternalRelation club;

  @Test
  void testController() {
    club = internalRelation();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      prepare();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    InternalRelationSelectionEvent event =
        new InternalRelationSelectionEvent(sut, MenuChoice.DELETE_EXTERNAL_CLUB, club);
    sut.onApplicationEvent(event);

    sut.delete();
    verify(getPageController()).showMessage("Gegevens zijn verwijderd");
  }

  @BeforeAll
  static void setup() {
   
    MockProvider.mock(InternalRelationService.class);
  };

  private void prepare() {
    reset(getPageController());
    sut = InternalRelationDeleteController.getInstance();
    getPageWithFxController(sut, PageName.INTERNAL_RELATION_DELETE);
  }

  private InternalRelation internalRelation() {
    InternalRelation r = new InternalRelation();
    Address a = r.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setAddressNumberAppendix("B");
    a.setCity("Ons Dorp");
    a.setPostalCode("1234 AA");

    r.setTitle("Algemeen");
    r.setModificationDate(LocalDate.of(2020, 12, 5));
    r.setContactName("Pietje Puk");
    r.setTelephoneNumber("06-01234567");
    r.setRelationNumber(8201);
    return r;
  }

}
