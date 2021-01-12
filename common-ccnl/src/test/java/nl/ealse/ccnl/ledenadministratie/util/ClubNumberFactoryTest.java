package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClubNumberFactoryTest {

  @Mock
  ExternalRelationClubRepository dao;

  @Test
  void newNumberTest() {
    ClubNumberFactory f = new ClubNumberFactory(dao);
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8200 && number < 8300,
        "Generated number: " + number.toString());
  }

}
