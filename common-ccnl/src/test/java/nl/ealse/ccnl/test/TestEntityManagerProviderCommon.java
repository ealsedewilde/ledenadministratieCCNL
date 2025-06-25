package nl.ealse.ccnl.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationContext;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import org.hibernate.cfg.JdbcSettings;

@Slf4j
public class TestEntityManagerProviderCommon implements EntityManagerProvider {
  
  private final EntityManagerFactory emf;
  private final EntityManager em;
  
  public TestEntityManagerProviderCommon() {
    emf = initialize();
    em = emf.createEntityManager();
  }

  @Override
  public void cleanup() {
    em.close();
   }

  @Override
  public void shutdown() {
    emf.close();
  }

  @Override
  public EntityManager getEntityManager() {
    return em;
  }
  
  private EntityManagerFactory initialize() {
    DatabaseLocation dbl = new DatabaseLocation();
    Optional<String> optUrl = dbl.getDataBaseUrl();
    if (optUrl.isPresent()) {
      Map<String, String> properties = new HashMap<>();
      properties.put("jakarta.persistence.schema-generation.database.action", "create");
      properties.put(JdbcSettings.JAKARTA_JDBC_URL, optUrl.get());
      properties.put(JdbcSettings.JAKARTA_JDBC_USER,
          ApplicationContext.getProperty("database.user"));
      properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD,
          ApplicationContext.getProperty("database.password", ""));
      return Persistence.createEntityManagerFactory("nl.ealse.ccnl.leden", properties);
     } else {
      String msg = "No database location provided.";
      log.error(msg);
      throw new ExceptionInInitializerError(msg);
    }
    
  }

}
