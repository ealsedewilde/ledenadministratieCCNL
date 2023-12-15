package nl.ealse.ccnl.ledenadministratie.util;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;

public class ClubNumberFactory extends NumberFactory {

  @Getter
  private static ClubNumberFactory instance = new ClubNumberFactory();
  
  private final ExternalRelationClubRepository dao = ExternalRelationClubRepository.getInstance();
  
  private ClubNumberFactory() {
    super(99, 8200);
    initialize();
  }

  private void initialize() {
    super.initialize(dao.getAllRelationNumbers());

  }

}
