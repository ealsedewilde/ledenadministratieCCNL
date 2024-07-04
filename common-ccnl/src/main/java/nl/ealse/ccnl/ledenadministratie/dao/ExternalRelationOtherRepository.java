package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;

public class ExternalRelationOtherRepository
    extends ExternalRelationRepository<ExternalRelationOther> {
  
  public ExternalRelationOtherRepository() {
    super(ExternalRelationOther.class);
  }

  @Override
  protected Object getPrimaryKey(ExternalRelationOther entity) {
    return entity.getRelationNumber();
  }

  public List<Number> getAllRelationNumbers() {
    TypedQuery<Number> query = getEntityManager()
        .createQuery("Select m.relationNumber from ExternalRelationOther m", Number.class);
    return query.getResultList();
  }

}
