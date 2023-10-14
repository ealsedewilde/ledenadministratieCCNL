package nl.ealse.ccnl.control.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.service.relation.InternalRelationService;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InternalRelationControllerTest extends FXMLBaseTest {

  private static InternalRelationService internalRelationService;


  private InternalRelationController sut;
  private InternalRelation rel;

  @Test
  void testController() {
    sut = new InternalRelationController(getPageController(), internalRelationService);
    rel = internalRelation();
    final AtomicBoolean ar = new AtomicBoolean();
    AtomicBoolean result = runFX(() -> {
      sut.setup();
      doTest();
      ar.set(true);
    }, ar);
    Assertions.assertTrue(result.get());
  }

  private void doTest() {
    InternalRelationSelectionEvent event =
        new InternalRelationSelectionEvent(sut, MenuChoice.AMEND_INTERNAL_RELATION, rel);
    sut.amendRelation(event);

    sut.save();
    verify(getPageController()).showMessage("Functiegegevens opgeslagen");

    FormController formController = getFormController();
    formController.nextPage();
    formController.previousPage();

  }

  @BeforeAll
  static void setup() {
    internalRelationService = mock(InternalRelationService.class);
  }

  private InternalRelation internalRelation() {
    InternalRelation r = new InternalRelation();
    Address a = r.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setAddressNumberAppendix("B");
    a.setCity("Ons Dorp");
    a.setPostalCode("1234 AA");

    r.setTitle("Ledenadministratie");
    r.setModificationDate(LocalDate.of(2020, 12, 5));
    r.setContactName("Pietje Puk");
    r.setTelephoneNumber("06-01234567");
    r.setRelationNumber(8506);
    return r;
  }
  
  private FormController getFormController() {
    try {
      return (FormController) FieldUtils.readDeclaredField(sut, "formController", true);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

}
