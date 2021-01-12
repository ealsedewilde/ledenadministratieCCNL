package nl.ealse.ccnl.ledenadministratie.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AdrressTest {

  @Test
  void addressNumberTest() {
    Address a = new Address();
    a.setAddress("straat");
    a.setAddressNumber("199");
    a.setAddressNumberAppendix("A");
    Assertions.assertEquals("straat 199A", a.getAddressAndNumber());
  }
}
