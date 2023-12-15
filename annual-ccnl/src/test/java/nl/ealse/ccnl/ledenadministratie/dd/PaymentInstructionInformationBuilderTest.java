package nl.ealse.ccnl.ledenadministratie.dd;

import java.math.BigDecimal;
import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentInstructionInformationBuilderTest {
  
  private PaymentInstructionInformationBuilder sut;
  
  @Test
  void testBuilder() {
    sut= new PaymentInstructionInformationBuilder();
    sut.metAantalTransacties(10)
    .metIncassodatum(LocalDate.now())
    .metPaymentInformation("test").metSomTransactieBedrag(BigDecimal.TEN);
    PaymentInstructionInformation4 result = sut.build();
    Assertions.assertNotNull(result);
  }

}
