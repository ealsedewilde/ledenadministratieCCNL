package nl.ealse.ccnl.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import nl.ealse.ccnl.database.config.DatabaseException;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.ccnl.ledenadministratie.dao.util.PersistenceInitializer;
import org.hibernate.Session;
import org.hibernate.cfg.JdbcSettings;

public class TestPersistenceInitializer implements PersistenceInitializer {
  // nl.ealse.ccnl.test TestPersistenceInitializer
  EntityManagerFactory emf;

  @Override
  public EntityManager initializePersistence() {
    if (emf == null) {
      String dbUrl = "jdbc:h2:mem:test";
      Map<String, String> properties = new HashMap<>();
      properties.put("jakarta.persistence.schema-generation.database.action", "create");
      properties.put(JdbcSettings.JAKARTA_JDBC_URL, dbUrl);
      properties.put(JdbcSettings.JAKARTA_JDBC_USER,
          ApplicationProperties.getProperty("database.user"));
      properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD,
          ApplicationProperties.getProperty("database.password", ""));
      emf = Persistence.createEntityManagerFactory("nl.ealse.ccnl.leden", properties);
    }
    EntityManager em = emf.createEntityManager();
    loadInitSql(em);
    return em;
  }

  @Override
  public void shutdown() {
    // nothing

  }

  private void loadInitSql(EntityManager em) {
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/init.sql")))) {
      Session session = em.unwrap(Session.class);
      session.doWork(connection -> {
        try {
          connection.setAutoCommit(false);
          Statement st = connection.createStatement();
          String line = reader.readLine();
          StringJoiner sj = new StringJoiner(" ");
          while (line != null) {
            line = line.trim();
            sj.add(line);
            if (line.endsWith(";")) {
              st.execute(sj.toString());
              sj = new StringJoiner(" ");
            }
            line = reader.readLine();
          }
          connection.commit();
        } catch (IOException e) {
          throw new DatabaseException("Error reading init.sql", e);
        }

      });
    } catch (IOException e) {
      throw new DatabaseException("Error reading init.sql", e);
    }

  }


}
