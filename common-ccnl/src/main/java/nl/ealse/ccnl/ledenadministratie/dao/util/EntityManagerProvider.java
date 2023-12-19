package nl.ealse.ccnl.ledenadministratie.dao.util;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;

/**
 * Utility to access the database.
 */
@UtilityClass
public class EntityManagerProvider {

  private static final PersistenceInitializer pi;

  static {
    ServiceLoader<PersistenceInitializer> serviceLoader =
        ServiceLoader.load(PersistenceInitializer.class);
    Optional<PersistenceInitializer> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      pi = first.get();
    } else {
      throw new ExceptionInInitializerError("No DatabasePropertiesProvider available");
    }
  }

  private ThreadLocal<EntityManager> threadLocal =
      ThreadLocal.withInitial(pi::initializePersistence);

  /**
   * Close all connections with the database.
   */
  public void close() {
    pi.shutdown();
  }

  public static EntityManager getEntityManager() {
    return threadLocal.get();
  }

}
