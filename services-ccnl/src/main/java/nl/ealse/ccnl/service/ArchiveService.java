package nl.ealse.ccnl.service;

import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ArchiveRepository;

/**
 * Remove obselete archive data.
 */
@Slf4j
public class ArchiveService {
 
  private final ArchiveRepository archiveRepository;

  public ArchiveService(ArchiveRepository archiveRepository) {
    log.info("Service created");
    this.archiveRepository = archiveRepository;
  }

  public void delete(int referenceYear) {
    archiveRepository.deleteObseleteArchivedMembers(referenceYear);
  }

}
