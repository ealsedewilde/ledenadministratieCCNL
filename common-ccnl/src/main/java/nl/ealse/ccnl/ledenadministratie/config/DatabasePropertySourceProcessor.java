package nl.ealse.ccnl.ledenadministratie.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class DatabasePropertySourceProcessor implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment,
      SpringApplication application) {
    /*
     * Properties that are managed by the user
     */
    System.out.println("Load User Properties");
    environment.getPropertySources().addFirst(new DatabasePropertySource(environment));
  }

}
