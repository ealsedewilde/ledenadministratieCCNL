package nl.ealse.ccnl.control.address;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressPropertyValueFactoryTest {

  private AddressPropertyValueFactory<InternalRelation> sut;

  @SuppressWarnings("unchecked")
  @Test
  void testCall() {
    sut = new AddressPropertyValueFactory<>();
    CellDataFeatures<InternalRelation, String> param = mock(CellDataFeatures.class);
    InternalRelation rel = new InternalRelation();
    Address a = rel.getAddress();
    a.setAddress("Straat");
    a.setAddressNumber("99");
    a.setAddressNumberAppendix("-II");
    a.setCity("Ons Dorp");
    a.setPostalCode("1234 AA");
    when(param.getValue()).thenReturn(rel);

    sut.setProperty("addressAndNumber");
    ObservableValue<String> result = sut.call(param);
    Assertions.assertEquals("Straat 99-II", result.getValue());

    sut.setProperty("city");
    result = sut.call(param);
    Assertions.assertEquals("Ons Dorp", result.getValue());

    sut.setProperty("postalCode");
    result = sut.call(param);
    Assertions.assertEquals("1234 AA", result.getValue());

  }

}
