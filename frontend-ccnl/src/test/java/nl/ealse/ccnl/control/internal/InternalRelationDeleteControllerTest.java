package nl.ealse.ccnl.control.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.control.menu.PageController;
import nl.ealse.ccnl.control.menu.PageName;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.InternalRelationService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import nl.ealse.javafx.FXMLMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InternalRelationDeleteControllerTest extends FXMLBaseTest<InternalRelationDeleteController> {

  private static PageController pageController;
  private static InternalRelationService service;

  private InternalRelationDeleteController sut;
  private InternalRelation club;

  @Test
  void testController() {
    sut = new InternalRelationDeleteController(service, pageController);
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
    verify(pageController).setMessage("Gegevens zijn verwijderd");
  }

  @BeforeAll
  static void setup() {
   
    pageController = mock(PageController.class);
    service = mock(InternalRelationService.class);
  };

  private void prepare() {
    try {
      getPage(sut, PageName.INTERNAL_RELATION_DELETE);
    } catch (FXMLMissingException e) {
      Assertions.fail(e.getMessage());
    }
  }

  private InternalRelation internalRelation() {
    InternalRelation r = new InternalRelation();
    Address a = r.getAddress();
    a.setAddress("straat");
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
