package nl.ealse.ccnl.ledenadministratie.dd;

import static org.mockito.Mockito.mock;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentInstructionInformationBuilderTest {
  
  private PaymentInstructionInformationBuilder sut;
  private IncassoProperties incassoProperties;
  
  @Test
  void testBuilder() {
    initIncassoProperties();
    sut= new PaymentInstructionInformationBuilder(incassoProperties);
    sut.metAantalTransacties(10)
    .metIncassodatum(LocalDate.now())
    .metPaymentInformation("test").metSomTransactieBedrag(BigDecimal.TEN);
    PaymentInstructionInformation4 result = sut.build();
    Assertions.assertNotNull(result);
  }

  
  private void initIncassoProperties() {
    EntityManager em = mock(EntityManager.class);
    incassoProperties = new IncassoProperties(em, new IncassoPropertiesInitializer(em));
    incassoProperties.load();
  }


}
