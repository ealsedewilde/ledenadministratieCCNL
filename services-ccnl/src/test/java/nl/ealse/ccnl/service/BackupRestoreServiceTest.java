package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.util.EntityManagerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BackupRestoreServiceTest {

  @TempDir
  File tempDir;

  private static EntityManager em;

  private static Query query;

  private static BackupRestoreService sut;

  @Test
  void testBackupDatabase() {
    List<String> result = new ArrayList<>();
    when(query.getResultList()).thenReturn(result);
    File testFile = new File(tempDir, "backup.zip");
    sut.backupDatabase(testFile);
    verify(query).getResultList();
  }

  @Test
  void testRestoreDatabase() throws IOException {
    setup();
    URL url = BackupRestoreService.class.getResource("/backup.zip");
    sut.restoreDatabase(new File(url.getFile()));
    verify(query, atLeastOnce()).executeUpdate();
  }

  @Test
  void testRestoreDatabase2() throws IOException {
    URL url = BackupRestoreService.class.getResource("/dummy.zip");
    boolean result = sut.restoreDatabase(new File(url.getFile()));
    Assertions.assertFalse(result);
  }

  @BeforeAll
  static void setup() {
    query = mock(Query.class);
    em = EntityManagerProvider.getEntityManager();
    when(em.createNativeQuery(any(String.class))).thenReturn(query);
    EntityTransaction t = mock(EntityTransaction.class);
    when(em.getTransaction()).thenReturn(t);
    sut = BackupRestoreService.getInstance();


  }

}
