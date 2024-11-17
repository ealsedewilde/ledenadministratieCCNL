package nl.ealse.ccnl.control.internal;

import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import nl.ealse.ccnl.control.menu.MenuChoice;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.form.FormController;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.test.FXMLBaseTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InternalRelationControllerTest extends FXMLBaseTest {

  private InternalRelationController sut;
  private InternalRelation rel;

  @Test
  void testController() {
    rel = internalRelation();

    Assertions.assertTrue(runFX(() -> {
      doTest();
      return Boolean.TRUE;
    }));
    
  }

  private void doTest() {
    sut = getTestSubject(InternalRelationController.class);
    InternalRelationSelectionEvent event =
        new InternalRelationSelectionEvent(sut, MenuChoice.AMEND_INTERNAL_RELATION, rel);
    sut.amendRelation(event);

    sut.save();
    verify(getPageController()).showMessage("Functiegegevens opgeslagen");

    FormController formController = getFormController();
    formController.nextPage();
    formController.previousPage();

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
