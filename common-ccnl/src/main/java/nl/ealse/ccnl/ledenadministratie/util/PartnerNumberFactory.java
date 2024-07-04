package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;

public class PartnerNumberFactory extends NumberFactory {

  public PartnerNumberFactory(ExternalRelationPartnerRepository dao) {
    super(99, 8500);
    initialize(dao);
  }

  private void initialize(ExternalRelationPartnerRepository dao) {
    super.initialize(dao.getAllRelationNumbers());

  }

}
