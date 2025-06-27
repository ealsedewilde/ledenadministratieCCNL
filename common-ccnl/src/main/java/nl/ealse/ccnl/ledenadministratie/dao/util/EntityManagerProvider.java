package nl.ealse.ccnl.ledenadministratie.dao.util;

import jakarta.persistence.EntityManager;

/**
 * Utility to access the database.
 */
public interface EntityManagerProvider {

  /**
   * Close all connections with the database for this Thread.
   */
  void cleanup();

  /**
   * Close all connections with the database.
   */
  void shutdown();

  /**
   * Get the EntityManger.
   * @return EntityManager  for the current thread.
   */
  EntityManager getEntityManager();
}
