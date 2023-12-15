package nl.ealse.ccnl.ledenadministratie.model.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;

public class InternalRelationRepository extends BaseRepository<InternalRelation> {
  @Getter
  private static InternalRelationRepository instance = new InternalRelationRepository();
  
  private InternalRelationRepository() {
    super(InternalRelation.class);
  }

  @Override
  protected Object getPrimaryKey(InternalRelation entity) {
    return entity.getRelationNumber();
  }

  public Optional<InternalRelation> findInternalRelationByTitleIgnoreCase(String title) {
    List<InternalRelation> result =
        executeQuery("SELECT M FROM InternalRelation M WHERE LOWER(M.title) = LOWER(?1)", title);
    if (result.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(result.get(0));
  }

  public List<Number> getAllRelationNumbers() {
    TypedQuery<Number> query =
        getEntityManager().createQuery("Select m.relationNumber from InternalRelation m", Number.class);
    return query.getResultList();
  }

  public List<String> getAllTitles() {
    TypedQuery<String> query =
        getEntityManager().createQuery("Select m.title from InternalRelation m", String.class);
    return query.getResultList();
  }

  public List<InternalRelation> findInternalRelationsByAddress(String searchValue) {
    return executeQuery(
        "SELECT M FROM InternalRelation M WHERE LOWER(M.address.street) LIKE LOWER(concat(?1, '%'))",
        searchValue);
  }

  public List<InternalRelation> findInternalRelationsByCity(String searchValue) {
    return executeQuery(
        "SELECT M FROM InternalRelation M WHERE LOWER(M.address.city) LIKE LOWER(concat(?1, '%'))",
        searchValue);
  }

  public List<InternalRelation> findInternalRelationsByTitle(String searchValue) {
    return executeQuery(
        "SELECT M FROM InternalRelation M WHERE LOWER(M.title) LIKE LOWER(concat(?1, '%'))",
        searchValue);
  }

  public List<InternalRelation> findInternalRelationsByPostalCode(String searchValue) {
    return executeQuery(
        "SELECT M FROM InternalRelation M WHERE LOWER(M.address.postalCode) = LOWER(?1)",
        searchValue);
  }

}
