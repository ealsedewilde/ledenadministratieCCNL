package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import org.springframework.stereotype.Component;

@Component
public class PartnerNumberFactory extends NumberFactory {

  public PartnerNumberFactory(ExternalRelationPartnerRepository dao) {
    super(100, 8500);
    initialize(dao);
  }

  private void initialize(ExternalRelationPartnerRepository dao) {
    super.initialize(dao.getAllRelationNumbers());

  }

}
