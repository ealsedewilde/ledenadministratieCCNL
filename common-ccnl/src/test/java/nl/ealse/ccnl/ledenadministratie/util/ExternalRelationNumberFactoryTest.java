package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalRelationNumberFactoryTest {

  @Test
  void newNumberTest() {
    ExternalRelationNumberFactory f = new ExternalRelationNumberFactory(new ExternalRelationOtherRepository());
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8400 && number < 8500,
        "Generated number: " + number.toString());
  }


}
