package nl.ealse.ccnl;

import javafx.stage.Stage;
import nl.ealse.ccnl.database.config.DbConfigurator;
import nl.ealse.ccnl.database.config.DbProperties;
import nl.ealse.javafx.SpringJavaFXBase;
import org.springframework.context.ApplicationEventPublisher;

/**
 * The JavaFX application.
 *
 * @author ealse
 */
public class JavaFXApplication extends SpringJavaFXBase {

  public JavaFXApplication() {
    super(LedenadministratieApplication.class);
  }

  /**
   * After this method, both SpringBoot and JavaFX are ready to run. This method gets called after
   * the init(). It will publish a StageReadyEvent to inform other components that the application
   * is ready to run.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    if (DbProperties.exists()) {
      startApplication(primaryStage);
    } else {
      DbConfigurator dbConfig = new DbConfigurator(() -> {
        startApplication(primaryStage);
        return null;
      });
      dbConfig.openDialog();
    }
  }
  
  private void startApplication(Stage primaryStage) {
    ApplicationEventPublisher publisher = initSpringBoot();
    publisher.publishEvent(new StageReadyEvent(primaryStage));
  }

}
