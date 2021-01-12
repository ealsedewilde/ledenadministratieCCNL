package nl.ealse.ccnl.ledenadministratie.util;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MemberNumberFactoryTest {

  @Mock
  MemberRepository dao;

  @Test
  void newNumberTest() {
    MemberNumberFactory f = new MemberNumberFactory(dao, "500");
    Integer number = f.getNewNumber();
    log.info("number: " + number.toString());
    Assertions.assertTrue(number > 0 && number < 500, "Generated number: " + number.toString());
  }


}
