package nl.ealse.ccnl.ledenadministratie.dao.util;

/**
 * Unit of work to excute in a database transaction.
 *
 * @param <E> Exception thrown by the unit of work
 */
@FunctionalInterface
public interface Procedure<E extends Exception> {

  /**
   * Execute the unit of work.
   *
   * @throws E Exception thrown by the unit of work
   */
  void execute() throws E;

}
