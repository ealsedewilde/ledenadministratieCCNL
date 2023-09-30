package nl.ealse.ccnl.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BackupRestoreService {

  /**
   * A block size large enough to hold a complete PDF.
   * This takes away the need of an intermediate SYSTEM_LOB_STREAM table 
   */
  private static final String BACKUP_SQL = "SCRIPT SIMPLE NOPASSWORDS DROP BLOCKSIZE 524288 TO '%s' COMPRESSION ZIP";
  private static final String RESTORE_SQL = "RUNSCRIPT FROM '%s' COMPRESSION ZIP";


  private final EntityManager em;

  public BackupRestoreService(EntityManager em) {
    log.info("Service created");
    this.em = em;
  }

  @Transactional
  public void backupDatabase(File backupName) {
    String queryString = String.format(BACKUP_SQL, backupName.getAbsolutePath());
    Query q = em.createNativeQuery(queryString);
    try {
      @SuppressWarnings("unchecked")
      List<String> result = q.getResultList();
      result.forEach(log::info);
    } catch(Exception e) {
      log.error("Backup failed", e);
      throw e;
    }
  }

  @Transactional
  public boolean restoreDatabase(File backupName) {
    boolean valid = restoreFileValid(backupName);
    if (valid) {
      String queryString = String.format(RESTORE_SQL, backupName.getAbsolutePath());
      Query q = em.createNativeQuery(queryString);
      try {
        int result = q.executeUpdate();
        log.info("Restore count: "+result);
      } catch(Exception e) {
        log.error("Restore failed", e);
        throw e;
      }
     
    }
    return valid;
  }
  
  public boolean restoreFileValid(File backupName) {
    try (ZipFile backup = new ZipFile(backupName)) {
      if (backup.size() == 1) {
        ZipEntry entry = backup.entries().nextElement();
        if ("script.sql".equals(entry.getName())) {
          return true;
        }
      }
      return false;
    } catch (IOException e) {
      log.error("Errror restoring database", e);
      throw new ServiceException("Errror restoring database", e);
    }

  }

}
