package nl.ealse.ccnl.service.relation;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;

public class ExternalClubService extends ExternalRelationService<ExternalRelationClub> {
  
  @Getter
  private static ExternalClubService instance = new ExternalClubService();

  private final ExternalRelationClubRepository clubDao;

  private ExternalClubService() {
    super(ClubNumberFactory::getInstance);
    this.clubDao = ExternalRelationClubRepository.getInstance();
  }

  @Override
  public ExternalRelationRepository<ExternalRelationClub> getDao() {
    return clubDao;
  }

}
