package nl.ealse.ccnl.service.relation;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.util.ClubNumberFactory;

@Slf4j
public class ExternalClubService extends ExternalRelationService<ExternalRelationClub> {
  {log.info("Service created");}

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
