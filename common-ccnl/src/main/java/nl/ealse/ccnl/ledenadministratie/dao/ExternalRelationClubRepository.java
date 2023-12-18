package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;

public class ExternalRelationClubRepository
    extends ExternalRelationRepository<ExternalRelationClub> {
  @Getter
  private static ExternalRelationClubRepository instance =
      new ExternalRelationClubRepository();
  
  private ExternalRelationClubRepository() {
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
