package nl.ealse.ccnl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ArchiveRepository;

/**
 * Remove obselete archive data.
 */
@Slf4j
@AllArgsConstructor
public class ArchiveService {
  {log.info("Service created");}
 
  private final ArchiveRepository archiveRepository;

  public void delete(int referenceYear) {
    archiveRepository.deleteObseleteArchivedMembers(referenceYear);
  }

}
