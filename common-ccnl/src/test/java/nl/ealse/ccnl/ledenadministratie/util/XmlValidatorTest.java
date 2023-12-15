package nl.ealse.ccnl.ledenadministratie.util;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class XmlValidatorTest {

  private final String xsd = "/camt.053.001.02.xsd";

  @Test
  void validationTest() {
    try {
      String xmlString = getXml("/booking.xml");
      boolean result = XmlValidator.validate(xsd, xmlString);
      Assertions.assertTrue(result);
    } catch (IOException e) {
      log.error("Error finding file", e);
      Assertions.fail(e.getMessage());
    }

  }

  private String getXml(String fileName) throws IOException {
    StringBuilder sb = new StringBuilder();
    byte[] buffer = new byte[512];
    try (InputStream is = getClass().getResourceAsStream(fileName)) {
      int len = is.read(buffer);
      while (len > 0) {
        String s = new String(buffer, 0, len);
        sb.append(s);
        len = is.read(buffer);
      }
      return sb.toString();
    } catch (IOException e) {
      log.error("Error reading file", e);
      throw e;
    }
  }


}
