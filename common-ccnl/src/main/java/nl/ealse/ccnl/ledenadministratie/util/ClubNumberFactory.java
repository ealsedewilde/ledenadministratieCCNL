package nl.ealse.ccnl.ledenadministratie.util;

import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import org.springframework.stereotype.Component;

@Component
public class ClubNumberFactory extends NumberFactory {

  public ClubNumberFactory(ExternalRelationClubRepository dao) {
    super(100, 8200);
    initialize(dao);
  }

  private void initialize(ExternalRelationClubRepository dao) {
    super.initialize(dao.getAllRelationNumbers());

  }

}
