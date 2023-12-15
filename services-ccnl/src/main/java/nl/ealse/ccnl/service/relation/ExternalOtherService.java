package nl.ealse.ccnl.service.relation;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.ExternalRelationNumberFactory;

public class ExternalOtherService extends ExternalRelationService<ExternalRelationOther> {
  
  @Getter
  private static ExternalOtherService instance = new ExternalOtherService();

  private final ExternalRelationOtherRepository externalRelationDao;

  private ExternalOtherService() {
    super(ExternalRelationNumberFactory::getInstance);
    this.externalRelationDao = ExternalRelationOtherRepository.getInstance();
  }

  @Override
  public ExternalRelationRepository<ExternalRelationOther> getDao() {
    return externalRelationDao;
  }

}
