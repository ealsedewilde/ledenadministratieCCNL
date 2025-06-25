package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExternalRelationNumberFactoryTest {
  
  @BeforeAll
  private static void setup() {
    ApplicationContext.start();
  }

  @Test
  void newNumberTest() {
    ExternalRelationNumberFactory f = new ExternalRelationNumberFactory(new ExternalRelationOtherRepository());
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8400 && number < 8500,
        "Generated number: " + number.toString());
  }


}
