package nl.ealse.ccnl.service.relation;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.ledenadministratie.util.ExternalRelationNumberFactory;

public class ExternalOtherService extends ExternalRelationService<ExternalRelationOther> {

  private final ExternalRelationOtherRepository externalRelationDao;

  public ExternalOtherService(ExternalRelationOtherRepository externalRelationDao,
      ExternalRelationNumberFactory numberFactory) {
    super(numberFactory);
    this.externalRelationDao = externalRelationDao;
  }

  @Override
  public ExternalRelationRepository<ExternalRelationOther> getDao() {
    return externalRelationDao;
  }

}
