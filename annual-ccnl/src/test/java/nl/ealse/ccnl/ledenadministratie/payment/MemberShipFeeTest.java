package nl.ealse.ccnl.ledenadministratie.payment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberShipFeeTest {
  
  @Test
  void testMemberShipFee() {
    Assertions.assertEquals(27.5, MemberShipFee.getIncasso());
    Assertions.assertEquals(30.0, MemberShipFee.getOverboeken());
  }
}
