package nl.ealse.ccnl.service.excelimport;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import nl.ealse.ccnl.ledenadministratie.excelimport.AddressMapping;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressMappingTest {
  
  @Test
  void testMapping1() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.30");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getStreet());
    Assertions.assertEquals("30", result.getAddressNumber());
  }
  
  @Test
  void testMapping2() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getStreet());
  }
  
  @Test
  void testMapping3() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.30 2");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getStreet());
    Assertions.assertEquals(" 2", result.getAddressNumberAppendix());
  }
  
  @Test
  void testMapping4() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.30A");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getStreet());
    Assertions.assertEquals("A", result.getAddressNumberAppendix());
  }

}
