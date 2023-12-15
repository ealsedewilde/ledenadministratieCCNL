package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;

public abstract class ExternalRelationRepository<T extends ExternalRelation>
    extends BaseRepository<T> {
  
  protected ExternalRelationRepository(Class<T> type) {
    super(type);
  }

  public List<T> findExternalRelationsByAddress(String searchValue) {
    String qlString = String.format(
        "SELECT M FROM %s M WHERE LOWER(M.address.street) LIKE LOWER(concat(?1, '%'))",
        getType().getSimpleName());
    return executeQuery(qlString, searchValue);
  }

  public List<T> findExternalRelationsByCity(String searchValue) {
    String qlString = String.format(
        "SELECT M FROM %s M WHERE LOWER(M.address.city) LIKE LOWER(concat('%', ?1, '%'))",
        getType().getSimpleName());
    return executeQuery(qlString, searchValue);
  }
  
  public List<T> findExternalRelationsByName(String searchValue) {
    String qlString = String.format(
        "SELECT M FROM %s M WHERE LOWER(M.relationName) LIKE LOWER(concat('%', ?1, '%'))",
        getType().getSimpleName());
    return executeQuery(qlString, searchValue);
  }
  
  public List<T> findExternalRelationsByPostalCode(String searchValue) {
    String qlString = String.format(
        "SELECT M FROM %s M WHERE LOWER(M.address.postalCode) = LOWER(?1)",
        getType().getSimpleName());
    return executeQuery(qlString, searchValue);
  }
  
  @Override
  protected Object getPrimaryKey(T entity) {
    return entity.getRelationNumber();
  }
}
