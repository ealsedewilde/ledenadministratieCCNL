package nl.ealse.ccnl.ledenadministratie.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

class DatabasePropertySourceTest {

  private ConfigurableEnvironment environment;
  private SpringApplication application;

  private DatabasePropertySourceProcessor sut;

  @Test
 void testPropertySource() {
    sut = new DatabasePropertySourceProcessor();
    sut.postProcessEnvironment(environment, application);
    String subject = null;
    do {
      subject = environment.getProperty("ccnl.mail.subject");
    } while (subject == null);
    Assertions.assertEquals("Opzegging lidmaatschap Citroën Club Nederland", subject);
  }
  
  @Test
  void testInitSql() throws IOException {
    File file = new File("db.properties");
    try {
      BufferedWriter out = Files.newBufferedWriter(file.toPath(), CREATE_NEW);
      out.write("db.locatie = jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
      out.close();
      System.out.println("done");
      sut = new DatabasePropertySourceProcessor();
      sut.postProcessEnvironment(environment, application);
      String subject = null;
      do {
        subject = environment.getProperty("ccnl.mail.subject");
      } while (subject == null);
      Assertions.assertEquals("Opzegging lidmaatschap Citroën Club Nederland", subject);
    } finally {
      file.deleteOnExit();
    }
  }

  @BeforeEach
  void setup() {
    environment = new StandardEnvironment();
    ConfigDataEnvironmentPostProcessor.applyTo(environment);
    application = new SpringApplication();
  }

}
