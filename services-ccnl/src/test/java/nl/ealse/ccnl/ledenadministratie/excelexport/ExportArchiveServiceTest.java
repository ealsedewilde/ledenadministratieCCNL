package nl.ealse.ccnl.ledenadministratie.excelexport;

import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.model.ArchiveId;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.dao.ArchiveRepository;
import nl.ealse.ccnl.test.util.ExcelPropertiesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExportArchiveServiceTest {
  
  @TempDir
  File tempDir;
  
  @Mock
  private ArchiveRepository archiveRepository;
  
  private CCNLColumnProperties properties;
  
  private ExportArchiveService sut;
  
  @Test
  void testService() {
    sut = new ExportArchiveService(archiveRepository, properties);
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
    properties = ExcelPropertiesFactory.newExcelProperties();
    setup();
  }
   
  private void setup() {
    List<ArchivedMember> archiveMembers = new ArrayList<>();
    ArchivedMember member = new ArchivedMember();
    archiveMembers.add(member);
    ArchiveId id = new ArchiveId();
    member.setId(id);
    member.setMember(null);
    id.setMemberNumber(1234);
    id.setArchiveYear(2018);
    Member m = new Member();
    member.setMember(m);
    when(archiveRepository.findAllByYearAndMemberNumber()).thenReturn(archiveMembers);
  }
  

}
