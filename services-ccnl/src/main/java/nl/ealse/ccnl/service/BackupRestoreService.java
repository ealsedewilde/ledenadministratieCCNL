package nl.ealse.ccnl.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  private static final String TEMP_FILE = "temp_backup.zip";

  /**
   * A block size large enough to hold a complete PDF. This takes away the need of an intermediate
   * SYSTEM_LOB_STREAM table
   */
  private static final String BACKUP_SQL = String
      .format("SCRIPT SIMPLE NOPASSWORDS DROP BLOCKSIZE 524288 TO '%s' COMPRESSION ZIP", TEMP_FILE);
  private static final String RESTORE_SQL = "RUNSCRIPT FROM '%s' COMPRESSION ZIP";


  private BackupRestoreService() {
    log.info("Service created");
  }

  /**
   * Backup the database to a zip file.
   * First create the zip to a local file, because that is fast.
   * Then move the file to the (Cloud) destination. 
   *
   * @param backupName - name of the zip file
   * @throws Exception - when creating backup goes wrong
   */
  public void backupDatabase(File backupName) throws Exception {
    EntityManager em = EntityManagerProvider.getEntityManager();
    Query q = em.createNativeQuery(BACKUP_SQL);
    try {
      @SuppressWarnings("unchecked")
      List<String> result = q.getResultList();
      result.forEach(log::info);
      Path src = Paths.get(TEMP_FILE);
      Path dest = backupName.toPath();
      Files.move(src, dest);
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
