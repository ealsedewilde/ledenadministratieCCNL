package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PartnerNumberFactoryTest {
  
  @BeforeAll
  private static void setup() {
    ApplicationContext.start();
  }

  @Test
  void newNumberTest() {
    PartnerNumberFactory f =  new PartnerNumberFactory(new ExternalRelationPartnerRepository());
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8500 && number < 8600,
        "Generated number: " + number.toString());
  }

}
