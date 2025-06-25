package nl.ealse.ccnl.ledenadministratie.util;

import java.math.BigDecimal;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AmountToPayTest {
  
  @BeforeAll
  private static void setup() {
    ApplicationContext.start();
  }
  
  @Test
  void amountToPayDefault() {
    String result = AmountToPay.amountToPayDefault();
    Assertions.assertEquals("35,00", result);
  }
  
  @Test
  void amountToPayAsString() {
    String result = AmountToPay.amountToPayAsString(BigDecimal.ZERO);
    Assertions.assertEquals("€ 35,00", result);
    result = AmountToPay.amountToPayAsString(new BigDecimal("30"));
    Assertions.assertEquals("€ 5,00", result);

  }

}
