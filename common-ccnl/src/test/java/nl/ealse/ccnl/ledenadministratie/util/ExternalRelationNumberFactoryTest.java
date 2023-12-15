package nl.ealse.ccnl.ledenadministratie.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalRelationNumberFactoryTest {

  @Test
  void newNumberTest() {
    ExternalRelationNumberFactory f = ExternalRelationNumberFactory.getInstance();
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8400 && number < 8500,
        "Generated number: " + number.toString());
  }


}
