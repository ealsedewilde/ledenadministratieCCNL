package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;

public class ExternalRelationNumberFactory extends NumberFactory {
  
  public ExternalRelationNumberFactory(ExternalRelationOtherRepository dao) {
    super(99, 8400);
    initialize(dao);
  }

  private void initialize(ExternalRelationOtherRepository dao) {
    super.initialize(dao.getAllRelationNumbers());

  }

}
