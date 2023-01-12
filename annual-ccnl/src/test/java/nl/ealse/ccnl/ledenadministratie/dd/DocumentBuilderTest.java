package nl.ealse.ccnl.ledenadministratie.dd;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DocumentBuilderTest {

  @Test
  void build() {
    EntityManager em = Mockito.mock(EntityManager.class);
    IncassoPropertiesInitializer initializer = new IncassoPropertiesInitializer(em);
    IncassoProperties props = new IncassoProperties(em, initializer);
    props.load();

    DocumentBuilder sut = new DocumentBuilder(props);
    LocalDateTime dtm = LocalDateTime.of(2020, 12, 5, 10, 0);
    sut.metCreateDate(dtm);
    sut.metNumberOfTransactions(2);
    sut.metControlSum(BigDecimal.valueOf(55.0));
    sut.metMessageId("someId");
    PaymentInstructionInformation4 paymentInstruction = new PaymentInstructionInformation4();
    sut.metPaymentInstructionInformation(paymentInstruction);
    Document d = sut.build();
    String id = d.getCstmrDrctDbtInitn().getGrpHdr().getMsgId();
    Assertions.assertEquals("someId", id);
  }


}
