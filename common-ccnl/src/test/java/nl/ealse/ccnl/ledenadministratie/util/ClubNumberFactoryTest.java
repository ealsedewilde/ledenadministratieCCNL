package nl.ealse.ccnl.ledenadministratie.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClubNumberFactoryTest {

  @Test
  void newNumberTest() {
    ClubNumberFactory f = ClubNumberFactory.getInstance();
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8200 && number < 8300,
        "Generated number: " + number.toString());
  }

}
