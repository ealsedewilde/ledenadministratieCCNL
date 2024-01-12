package nl.ealse.ccnl.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseProperties;
import nl.ealse.ccnl.ledenadministratie.dao.util.EntityManagerProvider;

/**
 * Database backup / restore.
 */
@Slf4j
public class BackupRestoreService {

  @Getter
  private static BackupRestoreService instance = new BackupRestoreService();

  /**
   * A block size large enough to hold a complete PDF. This takes away the need of an intermediate
   * SYSTEM_LOB_STREAM table
   */
  private static final String BACKUP_SQL =
      "SCRIPT SIMPLE NOPASSWORDS DROP BLOCKSIZE 524288 TO '%s' COMPRESSION ZIP";
  private static final String RESTORE_SQL = "RUNSCRIPT FROM '%s' COMPRESSION ZIP";


  private BackupRestoreService() {
    log.info("Service created");
  }

  /**
   * Backup the database to a zip file.
   *
   * @param backupName - name of the zip file
   */
  public void backupDatabase(File backupName) {
    String queryString = String.format(BACKUP_SQL, backupName.getAbsolutePath());
    EntityManager em = EntityManagerProvider.getEntityManager();
    Query q = em.createNativeQuery(queryString);
    try {
      @SuppressWarnings("unchecked")
      List<String> result = q.getResultList();
      result.forEach(log::info);
    } catch (Exception e) {
      log.error("Backup failed", e);
      throw e;
    }
  }

  /**
   * Restore the database from a zip file.
   *
   * @param backupName - name of the zip file
   * @return - true when successful
   */
  public boolean restoreDatabase(File backupName) {
    boolean valid = restoreFileValid(backupName);
    if (valid) {
      EntityManager em = EntityManagerProvider.getEntityManager();
      String queryString = String.format(RESTORE_SQL, backupName.getAbsolutePath());
      Query q = em.createNativeQuery(queryString);
      em.getTransaction().begin();
      try {
        int result = q.executeUpdate();
        log.info("Restore count: " + result);
        em.getTransaction().commit();
        em.clear();
        // reload to reflect the restored data
        DatabaseProperties.initialize();
      } catch (Exception e) {
        log.error("Restore failed", e);
        em.getTransaction().rollback();
        throw e;
      }

    }
    return valid;
  }

  private boolean restoreFileValid(File backupName) {
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
