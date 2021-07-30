package nl.ealse.ccnl.ledenadministratie.excelimport;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressMappingTest {
  
  @Test
  void testMapping1() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.30");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getAddress());
    Assertions.assertEquals("30", result.getAddressNumber());
  }
  
  @Test
  void testMapping2() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getAddress());
  }
  
  @Test
  void testMapping3() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.30 2");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getAddress());
    Assertions.assertEquals(" 2", result.getAddressNumberAppendix());
  }
  
  @Test
  void testMapping4() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("Dorpsstraat O.30A");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("Dorpsstraat O.", result.getAddress());
    Assertions.assertEquals("A", result.getAddressNumberAppendix());
  }
  
  @Test
  void testMapping5() {
    CCNLAdres ad = mock( CCNLAdres.class);
    when(ad.getStraat()).thenReturn("3e Dorpsstraat 9 2H");
    Address result = AddressMapping.mapAddress(ad);
    Assertions.assertEquals("3e Dorpsstraat", result.getAddress());
    Assertions.assertEquals(" 2H", result.getAddressNumberAppendix());
  }

}
