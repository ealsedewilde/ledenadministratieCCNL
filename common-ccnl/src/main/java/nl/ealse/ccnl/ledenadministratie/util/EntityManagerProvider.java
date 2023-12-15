package nl.ealse.ccnl.ledenadministratie.util;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.ServiceLoader;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * Utility to access the database.
 */
@UtilityClass
public class EntityManagerProvider {
  
  @Getter
  private final EntityManager entityManager;
  
  // load real database access  or mock in case of unit tests.
  static {
    ServiceLoader<PersistenceInitializer> serviceLoader =
        ServiceLoader.load(PersistenceInitializer.class);
    Optional<PersistenceInitializer> first = serviceLoader.findFirst();
    if (first.isPresent()) {
      entityManager = first.get().initializePersistence();
    } else {
      throw new ExceptionInInitializerError("No DatabasePropertiesProvider available");
    }
  }
  
  /**
   * Close all connections with the database.
   */
  public void close() {
    if (entityManager.getEntityManagerFactory().isOpen()) {
      entityManager.getEntityManagerFactory().close();
    }
  }

}
