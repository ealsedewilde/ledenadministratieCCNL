package nl.ealse.ccnl.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.dao.ArchiveRepository;

/**
 * Remove obselete archive data.
 */
@Slf4j
public class ArchiveService {
  
  @Getter
  private static ArchiveService instance = new ArchiveService(); 

  private final ArchiveRepository archiveRepository;

  private ArchiveService() {
    log.info("Service created");
    this.archiveRepository = ArchiveRepository.getInstance();
  }

  public void delete(int referenceYear) {
    archiveRepository.deleteObseleteArchivedMembers(referenceYear);
  }

}
