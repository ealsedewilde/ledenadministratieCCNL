package nl.ealse.ccnl.service.excelexport;

import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExportServiceTest {

  private static ExternalRelationPartnerRepository commercialPartnerRepository;
  private static ExternalRelationClubRepository externalRelationClubRepository;
  private static ExternalRelationOtherRepository externalRelationOtherRepository;
  private static InternalRelationRepository internalRelationRepository;
  private static MemberRepository memberRepository;

  @TempDir
  File tempDir;

  private static ExportService sut;

  @Test
  void exportALLTest() {
    File selectedFile = new File(tempDir, "exportAll.xlsx");
    try {
      sut.exportALL(selectedFile);
      Assertions.assertTrue(selectedFile.exists());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void exportNewTest() {
    File selectedFile = new File(tempDir, "exportNew.xlsx");
    try {
      sut.exportNew(selectedFile);
      Assertions.assertTrue(selectedFile.exists());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void exportCancelledTest() {
    File selectedFile = new File(tempDir, "exportCancelled.xlsx");
    try {
      sut.exportCancelled(selectedFile);
      Assertions.assertTrue(selectedFile.exists());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void exportOverdueTest() {
    File selectedFile = new File(tempDir, "exportOverdue.xlsx");
    try {
      sut.exportOverdue(selectedFile);
      Assertions.assertTrue(selectedFile.exists());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void paymentReminderTest() {
    File selectedFile = new File(tempDir, "paymentReminder.xlsx");
    try {
      sut.paymentReminderReport(selectedFile);
      Assertions.assertTrue(selectedFile.exists());
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @BeforeAll
  static void beforeAll() {
    initMocks();
    sut = ExportService.getInstance();
  }

  private static void initMocks() {
    commercialPartnerRepository = MockProvider.mock(ExternalRelationPartnerRepository.class);
    externalRelationClubRepository = MockProvider.mock(ExternalRelationClubRepository.class);
    externalRelationOtherRepository = MockProvider.mock(ExternalRelationOtherRepository.class);
    internalRelationRepository = MockProvider.mock(InternalRelationRepository.class);
    memberRepository = MockProvider.mock(MemberRepository.class);

    List<ExternalRelationClub> clubs = new ArrayList<>();
    when(externalRelationClubRepository.findAll()).thenReturn(clubs);

    List<ExternalRelationOther> others = new ArrayList<>();
    when(externalRelationOtherRepository.findAll()).thenReturn(others);

    List<ExternalRelationPartner> partners = new ArrayList<>();
    when(commercialPartnerRepository.findAll()).thenReturn(partners);

    List<InternalRelation> functies = new ArrayList<>();
    when(internalRelationRepository.findAll()).thenReturn(functies);

    List<Member> activeMembers = new ArrayList<>();
    when(memberRepository.findMembersByStatuses(
        EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP)))
            .thenReturn(activeMembers);
    LocalDate refDate = LocalDate.now();
    when(memberRepository.findNewMembers(refDate)).thenReturn(activeMembers);
    when(memberRepository.findMemberByMemberStatus(MembershipStatus.INACTIVE))
        .thenReturn(activeMembers);
    when(memberRepository.findMemberByMemberStatus(MembershipStatus.ACTIVE))
        .thenReturn(activeMembers);
    when(memberRepository.findMemberByMemberStatus(MembershipStatus.OVERDUE))
        .thenReturn(activeMembers);
    when(memberRepository.findMemberByMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP))
        .thenReturn(activeMembers);
  }

}
