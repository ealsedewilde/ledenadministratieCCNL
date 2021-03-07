package nl.ealse.ccnl.ledenadministratie.excel;

import nl.ealse.ccnl.test.util.ExcelPropertiesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CCNLColumnPropertiesTest {
  
  private CCNLColumnProperties sut;
  
  @Test
  void testProeprties() {
    sut = ExcelPropertiesFactory.newExcelProperties();
    StringBuilder sb = new StringBuilder();
    sb.append(sut.getPropertyAutomatischeIncasso());
    sb.append(sut.getPropertyErelid());
    sb.append(sut.getPropertyHeeftBetaald());
    sb.append(sut.getPropertyNietBetaald());
    sb.append(sut.getPropertyPasVerstuurd());
    Assertions.assertEquals("IErelidJNJ", sb.toString());
  }

}
