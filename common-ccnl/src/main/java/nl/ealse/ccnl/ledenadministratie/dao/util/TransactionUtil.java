package nl.ealse.ccnl.ledenadministratie.dao.util;

import jakarta.persistence.EntityTransaction;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility to handle some work in one database transaction.
 */
@Slf4j
@UtilityClass
public class TransactionUtil {
  
  /**
   * Handle work in one database transaction.
   *
   * @param <E> - the type of Exception
   * @param work - the work unit to handle in the transaction
   * @throws E exception thrown by the work unit
   */
  public <E extends Exception> void inTransction(Procedure<E> work) throws E {
    EntityTransaction transaction = EntityManagerProvider.getEntityManager().getTransaction();
    if (!transaction.isActive()) {
      try {
        transaction.begin();
        work.execute();
        transaction.commit();
      } catch (Exception e) {
        log.error("Transaction rollback", e);
        if (transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    } else {
      work.execute();
    }
  }


}
