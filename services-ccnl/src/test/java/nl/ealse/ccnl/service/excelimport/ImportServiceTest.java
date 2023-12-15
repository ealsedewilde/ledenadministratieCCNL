package nl.ealse.ccnl.service.excelimport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.net.URL;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetNotFoundException;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler.ImportSelection;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ImportServiceTest {
  private static MemberRepository memberRepository;
  private static ImportService sut;

  @Test
  void testImport() {
    reset(memberRepository);
    ImportSelection selection =
        new ImportSelection(true, true, true, true, true, ImportType.REPLACE);

    try {
      URL url = ImportService.class.getResource("/leden.xlsx");
      sut.importFromExcel(new File(url.getFile()), selection);
      verify(memberRepository, times(16)).save(any(Member.class));
    } catch (SheetNotFoundException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void testInvalidImport() {
    Exception thrown = Assertions.assertThrows(SheetNotFoundException.class, () -> {
      ImportSelection selection = new ImportSelection(true, true, true, true, true, ImportType.ADD);

      URL url = ImportService.class.getResource("/lidimport.xlsx");
      sut.importFromExcel(new File(url.getFile()), selection);
      verify(memberRepository, times(16)).save(any(Member.class));
    });
    Assertions.assertEquals("Excel tabblad 'Rel.&Advert.' niet gevonden in Excel bestand", thrown.getMessage());
  }

  @BeforeAll
  static void setup() {
    MockProvider.mock(ExternalRelationPartnerRepository.class);
    MockProvider.mock(ExternalRelationClubRepository.class);
    MockProvider.mock(ExternalRelationOtherRepository.class);
    MockProvider.mock(InternalRelationRepository.class);
    memberRepository = MockProvider.mock(MemberRepository.class);
    sut = ImportService.getInstance();
  }


}
