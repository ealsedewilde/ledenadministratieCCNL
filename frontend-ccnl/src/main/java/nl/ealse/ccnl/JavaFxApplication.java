package nl.ealse.ccnl;

import java.util.concurrent.ExecutionException;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.database.config.DbConfigurator;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;

@Slf4j
public class JavaFxApplication extends Application {
  
  private boolean dbLocationFieExists = DatabaseLocation.DB_LOCATION_FILE.exists();;

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (dbLocationFieExists) {
      publishEvent(primaryStage);
    } else {
      // There is no location for the database defined.
      // Start the dialog to configure it.
      DbConfigurator dbConfig = new DbConfigurator(() -> {
        ApplicationContext.start();
        publishEvent(primaryStage);
        return null;
      });
      dbConfig.openDialog();
    }
  }

  @Override
  public void init() throws Exception {
    if (dbLocationFieExists) {
      ApplicationContext.start();
    }
  }

  @Override
  public void stop() {
    ApplicationContext.stop();
  }

  private void publishEvent(Stage primaryStage) {
    try {
      if (StartContext.getUnique().get().booleanValue() &&
          StartContext.getInitialized().get().booleanValue()) {
        EventPublisher.publishEvent(new StageReadyEvent(primaryStage));
      } else {
        stop();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      stop();
    } catch (ExecutionException e) {
      log.error("Application fails to start", e);
      stop();
    }

  }

}
