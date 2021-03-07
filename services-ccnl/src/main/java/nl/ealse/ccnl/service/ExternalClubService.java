package nl.ealse.ccnl.service;

import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;
import org.springframework.stereotype.Service;

@Service
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
