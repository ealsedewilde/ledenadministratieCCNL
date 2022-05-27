package nl.ealse.ccnl.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringJoiner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BackupRestoreService {

  private static final String BACKUP_SQL = "SCRIPT SIMPLE NOPASSWORDS DROP TO '%s' COMPRESSION ZIP";

  private final EntityManager em;

  public BackupRestoreService(EntityManager em) {
    log.info("Service created");
    this.em = em;
  }

  @Transactional
  public void backupDatabase(File backupName) {
    String queryString = String.format(BACKUP_SQL, backupName.getAbsolutePath());
    Query q = em.createNativeQuery(queryString);
    @SuppressWarnings("unchecked")
    List<String> result = q.getResultList();
    result.forEach(log::info);
  }

  @Transactional
  public boolean restoreDatabase(File backupName) {
    try (ZipFile backup = new ZipFile(backupName)) {
      if (backup.size() == 1) {
        ZipEntry entry = backup.entries().nextElement();
        if ("script.sql".equals(entry.getName())) {
          executeRestore(backup, entry);
          return true;
        }
      }
      return false;
    } catch (IOException e) {
      log.error("Errror restoring database", e);
      throw new ServiceException("Errror restoring database", e);
    }

  }

  private void executeRestore(ZipFile backup, ZipEntry entry) throws IOException {
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(backup.getInputStream(entry)));
    String line = reader.readLine();
    StringJoiner sj = new StringJoiner(" ");
    while (line != null) {
      line = line.trim();
      if (!line.startsWith("--")) {
        sj.add(line);
        if (line.endsWith(";")) {
          executeCommand(sj.toString());
          sj = new StringJoiner(" ");
        }
      }
      line = reader.readLine();
    }
  }



  private void executeCommand(String command) {
    if (!command.startsWith("--")) {
      log.info(command);
      Query q2 = em.createNativeQuery(command);
      q2.executeUpdate();
    }

  }

}
