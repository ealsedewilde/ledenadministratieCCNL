package nl.ealse.ccnl.service.relation;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.PartnerNumberFactory;

public class CommercialPartnerService extends ExternalRelationService<ExternalRelationPartner> {
  
  @Getter
  private static CommercialPartnerService instance = new CommercialPartnerService();

  private final ExternalRelationPartnerRepository dao;

  private CommercialPartnerService() {
    super(PartnerNumberFactory::getInstance);
    this.dao = ExternalRelationPartnerRepository.getInstance();
  }

  @Override
  public ExternalRelationRepository<ExternalRelationPartner> getDao() {
    return dao;
  }

}
