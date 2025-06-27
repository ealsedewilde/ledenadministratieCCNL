package nl.ealse.ccnl;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.ealse.ccnl.database.config.DbConfigurator;
import nl.ealse.ccnl.database.config.DbProperties;
import nl.ealse.ccnl.event.support.EventProcessor;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;

public class JavaFxApplication extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (DbProperties.exists()) {
      EventPublisher.publishEvent(new StageReadyEvent(primaryStage));
    } else {
      DbConfigurator dbConfig = new DbConfigurator(() -> {
        ApplicationContext.start();
        EventPublisher.publishEvent(new StageReadyEvent(primaryStage));
        return null;
      });
      dbConfig.openDialog();
    }
  }

  @Override
  public void init() throws Exception {
    super.init();
    if (DatabaseLocation.DB_LOCATION_FILE.exists()) {
      ApplicationContext.start();
    }
    EventProcessor.getInstance().initialize();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    ApplicationContext.stop();
  }

}
