package nl.ealse.ccnl;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.ealse.ccnl.database.config.DbConfigurator;
import nl.ealse.ccnl.database.config.DbProperties;
import nl.ealse.ccnl.event.support.EventProcessor;
import nl.ealse.ccnl.event.support.EventPublisher;
import nl.ealse.ccnl.ioc.ComponentProvider;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

public class JavaFxApplication extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (DbProperties.exists()) {
      EventPublisher.publishEvent(new StageReadyEvent(primaryStage));
    } else {
      DbConfigurator dbConfig = new DbConfigurator(() -> {
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
      ComponentProvider.getComponent(TaskExecutor.class).execute(DatabaseProperties::initialize);
    }
    EventProcessor.getInstance().initialize();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    EntityManagerProvider.shutdown();
    System.exit(0);
  }

}
