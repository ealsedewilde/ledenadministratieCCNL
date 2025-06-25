package nl.ealse.ccnl.ledenadministratie.payment;

import nl.ealse.ccnl.test.ApplicationContextAware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberShipFeeTest extends ApplicationContextAware {
  
  @Test
  void testMemberShipFee() {
    Assertions.assertEquals(32.5, MemberShipFee.getIncasso());
    Assertions.assertEquals(35.0, MemberShipFee.getOverboeken());
  }
}
