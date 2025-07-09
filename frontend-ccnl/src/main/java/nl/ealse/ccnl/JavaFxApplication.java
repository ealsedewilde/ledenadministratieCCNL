package nl.ealse.ccnl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.database.config.DbConfigurator;
import nl.ealse.ccnl.database.config.DbProperties;
import nl.ealse.ccnl.event.support.EventProcessor;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.ConfigurationException;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;

@Slf4j
public class JavaFxApplication extends Application {

  private Future<Boolean> initialized;

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (DbProperties.exists()) {
      // There is a location for the database file
      publishEvent(primaryStage);
    } else {
      // There is no location for the database file
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
    // Initialize the EventProcessor
    initialized = Executors.newSingleThreadExecutor()
        .submit(() -> EventProcessor.getInstance().initialize(), Boolean.TRUE);
    if (DatabaseLocation.DB_LOCATION_FILE.exists()) {
      // There is a valid database file; so start connecting
      ApplicationContext.start();
    }
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    ApplicationContext.stop();
  }

  private void publishEvent(Stage primaryStage) {
    try {
      if (initialized.get().booleanValue()) {
        EventPublisher.publishEvent(new StageReadyEvent(primaryStage));
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      log.error("Application fails to start", e);
      throw new ConfigurationException("Application fails to start");
    }

  }

}
