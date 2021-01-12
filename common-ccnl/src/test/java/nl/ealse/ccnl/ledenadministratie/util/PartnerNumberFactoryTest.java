package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartnerNumberFactoryTest {

  @Mock
  ExternalRelationPartnerRepository dao;

  @Test
  void newNumberTest() {
    PartnerNumberFactory f = new PartnerNumberFactory(dao);
    Integer number = f.getNewNumber();
    Assertions.assertTrue(number >= 8500 && number < 8600,
        "Generated number: " + number.toString());
  }

}
