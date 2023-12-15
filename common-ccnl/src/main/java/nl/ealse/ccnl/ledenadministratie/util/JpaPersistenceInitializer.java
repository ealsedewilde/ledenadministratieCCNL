package nl.ealse.ccnl.ledenadministratie.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;
import org.hibernate.cfg.JdbcSettings;

/**
 * Provide JPS access to the database.
 */
@Slf4j
public class JpaPersistenceInitializer implements PersistenceInitializer {

  @Override
  public EntityManager initializePersistence() {
    DatabaseLocation dbl = new DatabaseLocation();
    Optional<String> optUrl = dbl.getDataBaseUrl();
    if (optUrl.isPresent()) {
      Map<String, String> properties = new HashMap<>();
      properties.put(JdbcSettings.JAKARTA_JDBC_URL, optUrl.get());
      properties.put(JdbcSettings.JAKARTA_JDBC_USER,
          ApplicationProperties.getProperty("database.user"));
      properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD,
          ApplicationProperties.getProperty("database.password", ""));
      EntityManagerFactory emf =
          Persistence.createEntityManagerFactory("nl.ealse.ccnl.leden", properties);
      return emf.createEntityManager();
    } else {
      String msg = "No database location provided.";
      log.error(msg);
      throw new ExceptionInInitializerError(msg);
    }
  }

}
