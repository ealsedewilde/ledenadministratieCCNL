package nl.ealse.ccnl.ledenadministratie.model.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;

public class ExternalRelationPartnerRepository
    extends ExternalRelationRepository<ExternalRelationPartner> {
  @Getter
  private static ExternalRelationPartnerRepository instance =
      new ExternalRelationPartnerRepository();
  
  private ExternalRelationPartnerRepository() {
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
