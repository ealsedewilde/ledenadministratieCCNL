package nl.ealse.ccnl.ledenadministratie.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
class XmlValidatorTest {

  private final Resource xsd = new ClassPathResource("camt.053.001.02.xsd");

  @Test
  void validationTest() {
    try {
      File xml = new ClassPathResource("booking.xml").getFile();
      String xmlString = getXml(xml);
      boolean result = XmlValidator.validate(xsd, xmlString);
      Assertions.assertTrue(result);
    } catch (IOException e) {
      log.error("Error finding file", e);
      Assertions.fail(e.getMessage());
    }

  }

  private String getXml(File file) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = reader.readLine();
      while (line != null) {
        sb.append(line);
        line = reader.readLine();
      }
    } catch (IOException e) {
      log.error("Error reading file", e);
      throw e;
    }
    return sb.toString();
  }


}
