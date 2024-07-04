package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

public class ExternalRelationClubRepository
    extends ExternalRelationRepository<ExternalRelationClub> {
  
  public ExternalRelationClubRepository() {
    super(ExternalRelationClub.class);
  }

  @Override
  protected Object getPrimaryKey(ExternalRelationClub entity) {
    return entity.getRelationNumber();
  }

  public List<Number> getAllRelationNumbers() {
    TypedQuery<Number> query = getEntityManager()
        .createQuery("Select m.relationNumber from ExternalRelationClub m", Number.class);
    return query.getResultList();
  }

}
