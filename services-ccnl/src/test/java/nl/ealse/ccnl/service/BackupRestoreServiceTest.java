package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class BackupRestoreServiceTest {

  @TempDir
  File tempDir;

  @Mock
  private EntityManager em;

  @Mock
  private Query query;

  @InjectMocks
  private BackupRestoreService sut;

  @Test
  void testBackupDatabase() {
    setup();
    List<String> result = new ArrayList<>();
    when(query.getResultList()).thenReturn(result);
    File testFile = new File(tempDir, "backup.zip");
    sut.backupDatabase(testFile);
    verify(query).getResultList();
  }

  @Test
  void testRestoreDatabase() throws IOException {
    setup();
    Resource r = new ClassPathResource("backup.zip");
    sut.restoreDatabase(r.getFile());
    verify(query, atLeastOnce()).executeUpdate();
  }

  @Test
  void testRestoreDatabase2() throws IOException {
    Resource r = new ClassPathResource("dummy.zip");
    boolean result = sut.restoreDatabase(r.getFile());
    Assertions.assertFalse(result);
  }

  void setup() {
    when(em.createNativeQuery(any(String.class))).thenReturn(query);
  }

}
