package nl.ealse.ccnl.ledenadministratie.dao.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;
import org.hibernate.cfg.JdbcSettings;

/**
 * Utility to access the database.
 */
@Slf4j
public class DefaultEntityManagerProvider implements EntityManagerProvider {

  /**
   * Holder for the EntityManager of the current thread.
   * Neede because an EntityManager is not thread safe.
   */
  private static final ThreadLocal<Optional<EntityManager>> threadLocal =
      ThreadLocal.withInitial(Optional::empty);
  
  private final EntityManagerFactory emf;
  
  public DefaultEntityManagerProvider() {
    emf = initialize();
  }
  
  /**
   * Connect to the specified database.
   * @return
   */
  private EntityManagerFactory initialize() {
    log.info("Start creating EntityManagerFactory");
    DatabaseLocation dbl = new DatabaseLocation();
    Optional<String> optUrl = dbl.getDataBaseUrl();
    if (optUrl.isPresent()) {
      Map<String, String> properties = new HashMap<>();
      properties.put(JdbcSettings.JAKARTA_JDBC_URL, optUrl.get());
      properties.put(JdbcSettings.JAKARTA_JDBC_USER,
          ApplicationContext.getProperty("database.user"));
      properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD,
          ApplicationContext.getProperty("database.password", ""));
      
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("nl.ealse.ccnl.leden", properties);
      log.info("EntityManagerFactory created");
      return factory;
    } else {
      String msg = "No database location provided.";
      log.error(msg);
      throw new ExceptionInInitializerError(msg);
    }
   
  }

  /**
   * Close all connections with the database for this Thread.
   */
  public void cleanup() {
    Optional<EntityManager> holder = threadLocal.get();
    if (holder.isPresent()) {
      holder.get().close();
      threadLocal.remove();
    }
  }

  /**
   * Close all connections with the database.
   */
  public void shutdown() {
    cleanup();
    emf.close();
  }

  public EntityManager getEntityManager() {
    Optional<EntityManager> holder = threadLocal.get();
    if (holder.isEmpty()) {
      holder = Optional.of(emf.createEntityManager());
      threadLocal.set(holder);
    }
    return holder.get();
  }
}
