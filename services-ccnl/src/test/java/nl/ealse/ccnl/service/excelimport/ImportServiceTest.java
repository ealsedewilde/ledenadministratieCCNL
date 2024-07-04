package nl.ealse.ccnl.service.excelimport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.net.URL;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetNotFoundException;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportHandler.ImportSelection;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
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
    Assertions.assertEquals("Excel tabblad 'Rel.&Advert.' niet gevonden in Excel bestand",
        thrown.getMessage());
  }

  @BeforeAll
  static void setup() {
    memberRepository = mock(MemberRepository.class);
    ImportHandler importHandler = new ImportHandler(memberRepository,
        mock(ExternalRelationClubRepository.class), mock(ExternalRelationOtherRepository.class),
        mock(ExternalRelationPartnerRepository.class), mock(InternalRelationRepository.class));
    sut = new ImportService(importHandler);
  }


}
