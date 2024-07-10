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
      pi = new JpaPersistenceInitializer();
    }
  }

  private static final ThreadLocal<Optional<EntityManager>> threadLocal =
      ThreadLocal.withInitial(Optional::empty);

  /**
   * Close all connections with the database for this Thread.
   */
  public void cleanup() {
    Optional<EntityManager> holder = threadLocal.get();
    if (holder.isPresent()) {
      holder.get().close();
    }
    threadLocal.remove();
  }

  /**
   * Close all connections with the database.
   */
  public void shutdown() {
    cleanup();
    pi.shutdown();
  }

  public static EntityManager getEntityManager() {
    Optional<EntityManager> holder = threadLocal.get();
    if (holder.isEmpty()) {
      holder = Optional.of(pi.initializePersistence());
      threadLocal.set(holder);
    }
    return holder.get();
  }
}
