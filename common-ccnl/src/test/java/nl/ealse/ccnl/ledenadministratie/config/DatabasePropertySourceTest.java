package nl.ealse.ccnl.ledenadministratie.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

class DatabasePropertySourceTest {

  private static ConfigurableEnvironment environment;
  private static SpringApplication application;

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

  @BeforeAll
  static void setup() {
    environment = new StandardEnvironment();
    ConfigDataEnvironmentPostProcessor.applyTo(environment);
    application = new SpringApplication();
  }

}
