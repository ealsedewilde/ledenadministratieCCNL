package nl.ealse.ccnl.service.relation;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.ledenadministratie.util.PartnerNumberFactory;

@Slf4j
public class CommercialPartnerService extends ExternalRelationService<ExternalRelationPartner> {

  private final ExternalRelationPartnerRepository dao;

  public CommercialPartnerService(ExternalRelationPartnerRepository dao, PartnerNumberFactory numberFactory) {
    super(numberFactory);
    log.info("Service created");
    this.dao = dao;
  }

  @Override
  public ExternalRelationRepository<ExternalRelationPartner> getDao() {
    return dao;
  }

}
