package nl.ealse.ccnl.ledenadministratie.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PartnerNumberFactoryTest {

  @Test
  void newNumberTest() {
    PartnerNumberFactory f = PartnerNumberFactory.getInstance();
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8500 && number < 8600,
        "Generated number: " + number.toString());
  }

}
