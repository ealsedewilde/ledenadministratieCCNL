package nl.ealse.ccnl.service.relation;

import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;

public class ExternalClubService extends ExternalRelationService<ExternalRelationClub> {

  private final ExternalRelationClubRepository clubDao;

  public ExternalClubService(ExternalRelationClubRepository clubDao,
      ClubNumberFactory numberFactory) {
    super(numberFactory);
    this.clubDao = clubDao;
  }

  @Override
  public ExternalRelationRepository<ExternalRelationClub> getDao() {
    return clubDao;
  }

}
