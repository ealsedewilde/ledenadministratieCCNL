package nl.ealse.ccnl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.control.HandledTask;
import nl.ealse.ccnl.ioc.ComponentFactory;
import nl.ealse.ccnl.ioc.DefaultComponentFactory;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.ContextInitializer;
import nl.ealse.ccnl.ledenadministratie.dao.SettingRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.DefaultEntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

/**
 * Production initializer of the {@link ApplicationContext}.
 */
@Slf4j
public class DefaultContextInitializer implements ContextInitializer {

  /**
   * The immutable properties for the application.
   */
  @Getter
  private final Properties properties = new Properties();

  /**
   * The properties/preferences for the application that can be changed by the user.
   */
  @Getter
  private Properties preferences = new Properties();

  /**
   * Simple Inversion of Control implementation. This IoC works via recursive calls to the single
   * constructor of a component. At the deepest level these components have a default (no arguments)
   * constructor. The created components are singletons.
   */
  private final ComponentFactory componentFactory = new DefaultComponentFactory();

  /**
   * Request a singleton component. Does not reset the state of a stateful component!
   */
  public <T> T getComponent(Class<T> clazz) {
    return componentFactory.getComponent(clazz);
  }

  /**
   * Provider for an EntityManger per thread.
   */
  @Getter
  private EntityManagerProvider entityManagerProvider;

  private void loadProperties(String location) {
    try (InputStream is = DefaultContextInitializer.class.getResourceAsStream(location)) {
      Properties props = new Properties();
      props.load(is);
      properties.putAll(props);
    } catch (IOException e) {
      log.error("failed to load application.properties", e);
    }

  }

  /**
   * For initial load and reload of the preferences. Reload is required after a change by the user
   * or a database restore.
   */
  @Override
  public void reloadPreferences() {
    SettingRepository dao = new SettingRepository();
    List<Setting> settings = dao.findByOrderBySettingsGroupAscKeyAsc();
    settings.forEach(setting -> preferences.put(setting.getId(), setting.getValue()));
  }

  /**
   * Initialize the ApplicationContext for the application.
   */
  @Override
  public void start() {
    try {
      // load the application.properties in the current Thread
      loadProperties("/application.properties");
      
      // Only try to connect to the database when this is the only instance of this application.
      if (StartContext.getUnique().get().booleanValue()) {
        // Setting up a database connection is a heavy operation
        // It should not block the start of the user interface
        StartContext.getThreadPool().execute(new DatabaseTask());
      }
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      log.error("Failed to initialize the application", e);
      System.exit(0);
    }

  }
  
  private class DatabaseTask extends HandledTask {

    @Override
    protected String call() throws Exception {
      entityManagerProvider = new DefaultEntityManagerProvider();
      reloadPreferences();
      loadProperties("/excel.properties");
      return "Database is gestart";
    }
    
  }


}
