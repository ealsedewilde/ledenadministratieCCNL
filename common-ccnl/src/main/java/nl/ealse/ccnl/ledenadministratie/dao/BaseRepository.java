package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;

/**
 * Super class of all database repositories.
 *
 * @param <T> persitent entity for the repository
 */
public abstract class BaseRepository<T> {

  @Getter
  private final Class<T> type;

  protected BaseRepository(Class<T> type) {
    this.type = type;
  }

  protected EntityManager getEntityManager() {
    return EntityManagerProvider.getEntityManager();
  }

  /**
   * Delete on persisten entity.
   *
   * @param entity - the entity to remove
   */
  public void delete(T entity) {
    TransactionUtil.inTransction(() -> getEntityManager().remove(entity));
  }

  /**
   * Persist an entity.
   *
   * @param entity - the entity to persist
   */
  public void save(T entity) {
    TransactionUtil.inTransction(() -> {
      EntityManager em = getEntityManager();
      if (getEntityManager().contains(entity) || em.find(type, getPrimaryKey(entity)) != null) {
        em.merge(entity);
      } else {
        em.persist(entity);
      }
    });
  }

  /**
   * Persist an entity.
   *
   * @param entity - the entity to persist
   * @return the persisted entity
   */
  public T saveAndFlush(T entity) {
    save(entity);
    return getEntityManager().find(type, getPrimaryKey(entity));
  }

  /**
   * Find a specific occrrence of a specific netity.
   *
   * @param id the primary key of the entity to find
   * @return the result of the database query; could be empty
   */
  public Optional<T> findById(Object id) {
    return Optional.ofNullable(getEntityManager().find(type, id));
  }

  /**
   * Delete one entity.
   *
   * @param id - the primary key of the entity to delete
   */
  public void deleteById(Object id) {
    Optional<T> m = findById(id);
    if (m.isPresent()) {
      getEntityManager().remove(m.get());
    }
  }

  /**
   * Count the rows for a specific entity.
   *
   * @return the rows counted
   */
  public long count() {
    String qlString = String.format("Select count(m) from %s m", type.getSimpleName());
    TypedQuery<Number> query = getEntityManager().createQuery(qlString, Number.class);
    List<Number> r = query.getResultList();
    return r.get(0).longValue();
  }

  /**
   * Find all occurrences of an entity.
   *
   * @return all occurrences of a specific entity.
   */
  public List<T> findAll() {
    String qlString = String.format("SELECT M FROM %s M", type.getSimpleName());
    return executeQuery(qlString);
  }

  /**
   * Save the persistent entities in the supplied collection.
   *
   * @param entities - the entities to save
   */
  public void saveAll(List<T> entities) {
    TransactionUtil.inTransction(() -> entities.forEach(getEntityManager()::persist));
  }

  /**
   * Delete the persistent entities in the supplied collection.
   *
   * @param entities - the entities to delete
   */
  public void deleteAll(List<T> entities) {
    TransactionUtil
        .inTransction(() -> entities.forEach(entity -> getEntityManager().remove(entity)));

  }


  protected List<T> executeQuery(String qlString, Object... parms) {
    TypedQuery<T> query = getEntityManager().createQuery(qlString, type);
    for (int ix = 0; ix < parms.length; ix++) {
      query.setParameter(ix + 1, parms[ix]);
    }
    return query.getResultList();
  }

  protected abstract Object getPrimaryKey(T entity);


}
