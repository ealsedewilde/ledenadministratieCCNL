package nl.ealse.ccnl.ledenadministratie.dd;

import static org.mockito.Mockito.mock;
import jakarta.persistence.EntityManager;
import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransactionInformation9;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DirectDebitTransactionInformationBuilderTest {
  
  private DirectDebitTransactionInformationBuilder sut;
  
  private IncassoProperties incassoProperties;
  
  @Test
  void testBuilder() {
    initIncassoProperties();
    sut = new DirectDebitTransactionInformationBuilder(incassoProperties);
    try {
      sut.metDibiteurIBAN("NL54ASNB0709093276");
      sut.metDibiteurNaam("Tester");
      sut.metLidnummer(1234);
      DirectDebitTransactionInformation9 result = sut.build();
      Assertions.assertNotNull(result);
    } catch (InvalidIbanException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  
  private void initIncassoProperties() {
    EntityManager em = mock(EntityManager.class);
    incassoProperties = new IncassoProperties(em, new IncassoPropertiesInitializer(em));
    incassoProperties.load();
  }


}
