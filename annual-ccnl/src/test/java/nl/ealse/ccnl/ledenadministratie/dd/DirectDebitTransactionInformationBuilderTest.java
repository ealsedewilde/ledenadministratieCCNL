package nl.ealse.ccnl.ledenadministratie.dd;

import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransactionInformation9;
import nl.ealse.ccnl.test.ApplicationContextAware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DirectDebitTransactionInformationBuilderTest extends ApplicationContextAware {
  
  private DirectDebitTransactionInformationBuilder sut;
  
  @Test
  void testBuilder() {
    sut = new DirectDebitTransactionInformationBuilder();
    try {
      sut.metDibiteurIBAN("NL54ASNB0709093276", null);
      sut.metDibiteurNaam("Tester");
      sut.metLidnummer(1234);
      DirectDebitTransactionInformation9 result = sut.build();
      Assertions.assertNotNull(result);
    } catch (InvalidIbanException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
