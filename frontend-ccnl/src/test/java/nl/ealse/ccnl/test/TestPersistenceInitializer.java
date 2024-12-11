package nl.ealse.ccnl.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.ccnl.ledenadministratie.dao.util.PersistenceInitializer;
import org.hibernate.cfg.JdbcSettings;

public class TestPersistenceInitializer implements PersistenceInitializer {
  // nl.ealse.ccnl.test TestPersistenceInitializer
  @Override
  public EntityManager initializePersistence() {
    String dbUrl = "jdbc:h2:mem:test";
    Map<String, String> properties = new HashMap<>();
    properties.put("jakarta.persistence.schema-generation.database.action", "create");
    properties.put(JdbcSettings.JAKARTA_JDBC_URL, dbUrl);
    properties.put(JdbcSettings.JAKARTA_JDBC_USER,
        ApplicationProperties.getProperty("database.user"));
    properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD,
        ApplicationProperties.getProperty("database.password", ""));
    EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("nl.ealse.ccnl.leden", properties);
    return emf.createEntityManager();
  }

  @Override
  public void shutdown() {
    // nothing

  }

}
