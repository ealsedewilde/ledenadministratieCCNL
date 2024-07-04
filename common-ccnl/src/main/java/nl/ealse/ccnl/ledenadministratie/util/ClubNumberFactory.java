package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;

public class ClubNumberFactory extends NumberFactory {
  
   public ClubNumberFactory(ExternalRelationClubRepository dao) {
    super(99, 8200);
    initialize(dao);
  }

  private void initialize(ExternalRelationClubRepository dao) {
    super.initialize(dao.getAllRelationNumbers());

  }

}
