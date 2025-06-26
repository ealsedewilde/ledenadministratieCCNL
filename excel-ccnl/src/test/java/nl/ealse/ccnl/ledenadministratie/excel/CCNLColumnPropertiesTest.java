package nl.ealse.ccnl.ledenadministratie.excel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CCNLColumnPropertiesTest {
  
  @Test
  void testProeprties() {
    StringBuilder sb = new StringBuilder();
    sb.append(CCNLColumnProperties.getPropertyAutomatischeIncasso());
    sb.append(CCNLColumnProperties.getPropertyErelid());
    sb.append(CCNLColumnProperties.getPropertyHeeftBetaald());
    sb.append(CCNLColumnProperties.getPropertyNietBetaald());
    sb.append(CCNLColumnProperties.getPropertyPasVerstuurd());
    Assertions.assertEquals("IErelidJNJ", sb.toString());
  }

}
