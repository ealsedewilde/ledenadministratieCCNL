package nl.ealse.ccnl.ledenadministratie.util;

import jakarta.persistence.EntityManager;

/**
 * Implementers provider access to the database.
 */
public interface PersistenceInitializer {
  
  /**
   * Provide access to the database.
   *
   * @return access to the database
   */
  EntityManager initializePersistence();

}
