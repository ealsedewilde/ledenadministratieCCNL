package nl.ealse.ccnl;

import javafx.application.Application;
import nl.ealse.javafx.FXMLLoaderBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "nl.ealse.ccnl.ledenadministratie.model.dao", bootstrapMode = BootstrapMode.LAZY)
@EnableAsync
@Import(FXMLLoaderBean.class)
public class LedenadministratieApplication {

  /**
   * Start the JavaFX application.
   * 
   * @param args - commandline args
   */
  public static void main(String[] args) {
    Application.launch(JavaFXApplication.class, args);
  }

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(25);
    return executor;
  }

}
