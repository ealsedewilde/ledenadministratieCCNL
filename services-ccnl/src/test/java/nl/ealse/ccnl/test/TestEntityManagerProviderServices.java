package nl.ealse.ccnl.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

public class TestEntityManagerProviderServices implements EntityManagerProvider {
  
  private final EntityManager em =  mock(EntityManager.class);
  
  public TestEntityManagerProviderServices() {
    EntityTransaction t = mock(EntityTransaction.class);
    when(em.getTransaction()).thenReturn(t);
  }

  @Override
  public void cleanup() {
    // No implementation
  }

  @Override
  public void shutdown() {
    // No implementation
  }

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

}
