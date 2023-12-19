package nl.ealse.ccnl.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import nl.ealse.ccnl.ledenadministratie.dao.util.PersistenceInitializer;

public class TestPersistenceInitializer implements PersistenceInitializer{

  @Override
  public EntityManager initializePersistence() {
    EntityManager em =  mock(EntityManager.class);
    EntityTransaction t = mock(EntityTransaction.class);
    when(em.getTransaction()).thenReturn(t);
    return em;

  }

  @Override
  public void shutdown() {
    // nothing
    
  }

}
