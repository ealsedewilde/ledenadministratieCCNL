package nl.ealse.ccnl.ledenadministratie.util;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AmountFormatterTest {
  
  @Test
  void testFormat() {
    String result = AmountFormatter.format(BigDecimal.valueOf(27.5));
    Assertions.assertEquals("€ 27,50", result);
  }
  
  @Test
  void testParse() {
    try {
      double result = AmountFormatter.parse("€ 27,50");
      Assertions.assertEquals(27.5, result);
    } catch (NumberFormatException e) {
      Assertions.fail(e.getMessage());
    }
    
  }

}
