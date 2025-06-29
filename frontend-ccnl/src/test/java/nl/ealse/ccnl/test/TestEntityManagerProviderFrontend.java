package nl.ealse.ccnl.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import org.hibernate.cfg.JdbcSettings;

public class TestEntityManagerProviderFrontend implements EntityManagerProvider {

  private final EntityManagerFactory emf;
  private final EntityManager em;
  private final Properties applicationProperties;

  public TestEntityManagerProviderFrontend(Properties applicationProperties) {
    this.applicationProperties = applicationProperties;
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
    Map<String, String> properties = new HashMap<>();
    properties.put("jakarta.persistence.schema-generation.database.action", "create");
    properties.put(JdbcSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:test");
    properties.put(JdbcSettings.JAKARTA_JDBC_USER, applicationProperties.getProperty("database.user"));
    properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD,
        applicationProperties.getProperty("database.password", ""));
    
    properties.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
    properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    return Persistence.createEntityManagerFactory("nl.ealse.ccnl.leden", properties);

  }


}
