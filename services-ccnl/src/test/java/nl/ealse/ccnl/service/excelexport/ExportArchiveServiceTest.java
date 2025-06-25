package nl.ealse.ccnl.service.excelexport;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.dao.ArchiveRepository;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.test.ApplicationContextAware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExportArchiveServiceTest extends ApplicationContextAware {
  
  @TempDir
  File tempDir;
  
  @Mock
  private ArchiveRepository archiveRepository;
  
  private ExportArchiveService sut;
  
  @Test
  void testService() {
    try {
      File f = new File(tempDir, "archief.xlsx");
      sut.export(f);
      Assertions.assertTrue(f.exists());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }
  
  @BeforeEach
  void before() {
    archiveRepository = mock(ArchiveRepository.class);
    sut = new ExportArchiveService(archiveRepository);
    setup();
  }
   
  private void setup() {
    List<ArchivedMember> archiveMembers = new ArrayList<>();
    Member m = new Member();
    m.setMemberNumber(1234);
    ArchivedMember member = new ArchivedMember(m);
    archiveMembers.add(member);
    when(archiveRepository.findAllByYearAndMemberNumber()).thenReturn(archiveMembers);
  }
  

}
