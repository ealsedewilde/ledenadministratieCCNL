package nl.ealse.ccnl.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;
import nl.ealse.ccnl.ledenadministratie.util.PersistenceInitializer;
import org.hibernate.cfg.JdbcSettings;

@Slf4j
public class TestPersistenceInitializer implements PersistenceInitializer {
  // nl.ealse.ccnl.test TestPersistenceInitializer
  @Override
  public EntityManager initializePersistence() {
    DatabaseLocation dbl = new DatabaseLocation();
    Optional<String> optUrl = dbl.getDataBaseUrl();
    if (optUrl.isPresent()) {
      Map<String, String> properties = new HashMap<>();
      properties.put("jakarta.persistence.schema-generation.database.action", "create");
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
