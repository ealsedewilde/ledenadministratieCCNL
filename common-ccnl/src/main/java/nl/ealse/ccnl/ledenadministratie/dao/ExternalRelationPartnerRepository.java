package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

public class ExternalRelationPartnerRepository
    extends ExternalRelationRepository<ExternalRelationPartner> {
  
  public ExternalRelationPartnerRepository() {
    super(ExternalRelationPartner.class);
  }

  @Override
  protected Object getPrimaryKey(ExternalRelationPartner entity) {
    return entity.getRelationNumber();
  }

  public List<Number> getAllRelationNumbers() {
    TypedQuery<Number> query = getEntityManager()
        .createQuery("Select m.relationNumber from ExternalRelationPartner m", Number.class);
    return query.getResultList();
  }

}
