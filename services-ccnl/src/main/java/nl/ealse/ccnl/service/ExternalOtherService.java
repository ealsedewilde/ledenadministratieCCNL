package nl.ealse.ccnl.service;

import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.ExternalRelationNumberFactory;
import org.springframework.stereotype.Service;

@Service
public class ExternalOtherService extends ExternalRelationService<ExternalRelationOther> {

  private final ExternalRelationOtherRepository externalRelationDao;

  public ExternalOtherService(ExternalRelationOtherRepository clubDao,
      ExternalRelationNumberFactory numberFactory) {
    super(numberFactory);
    this.externalRelationDao = clubDao;
  }

  @Override
  public ExternalRelationRepository<ExternalRelationOther> getDao() {
    return externalRelationDao;
  }

}
