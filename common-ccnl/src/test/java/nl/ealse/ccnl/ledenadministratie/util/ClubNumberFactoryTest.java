package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClubNumberFactoryTest {

  @Test
  void newNumberTest() {
    ClubNumberFactory f = new ClubNumberFactory(new ExternalRelationClubRepository());
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8200 && number < 8300,
        "Generated number: " + number.toString());
  }

}
