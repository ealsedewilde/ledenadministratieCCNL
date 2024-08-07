package nl.ealse.ccnl.ledenadministratie.util;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberNumberFactoryTest {
  
  @Test
  void newNumberTest() {
    MemberNumberFactory f = new MemberNumberFactory(new MemberRepository());
    Integer number = f.getNewNumber();
    log.info("number: " + number.toString());
    Assertions.assertTrue(number > 0 && number < 500, "Generated number: " + number.toString());
  }


}
