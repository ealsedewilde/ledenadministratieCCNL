package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExternalRelationNumberFactoryTest {

  @Mock
  ExternalRelationOtherRepository dao;

  @Test
  void newNumberTest() {
    ExternalRelationNumberFactory f = new ExternalRelationNumberFactory(dao);
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8400 && number < 8500,
        "Generated number: " + number.toString());
  }


}
