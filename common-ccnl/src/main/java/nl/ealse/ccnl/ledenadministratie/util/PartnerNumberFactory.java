package nl.ealse.ccnl.ledenadministratie.util;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;

public class PartnerNumberFactory extends NumberFactory {

  @Getter
  private static PartnerNumberFactory instance = new PartnerNumberFactory();

  private final ExternalRelationPartnerRepository dao =
      ExternalRelationPartnerRepository.getInstance();

  private PartnerNumberFactory() {
    super(99, 8500);
    initialize();
  }

  private void initialize() {
    super.initialize(dao.getAllRelationNumbers());

  }

}
