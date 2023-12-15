package nl.ealse.ccnl.service.excelexport;

import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
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

class ExportAddressServiceTest {

  private static final EnumSet<MembershipStatus> statuses =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP);

  private static MemberRepository memberRepository;

  private static ExportAddressService sut;

  @TempDir
  File tempDir;

  @Test
  void testService() {
    try {
      File cardFile = new File(tempDir, "card.xlsx");
      sut.generateCardAddressFile(cardFile);
      Assertions.assertTrue(cardFile.exists());
      File magazineFile = new File(tempDir, "magazine.xlsx");
      sut.generateMagazineAddressFile(magazineFile);
      Assertions.assertTrue(magazineFile.exists());
      File onNumberFile = new File(tempDir, "onNumberFile.xlsx");
      sut.generateMemberListFileByNumber(onNumberFile);
      Assertions.assertTrue(onNumberFile.exists());
      File onNameFile = new File(tempDir, "onNameFile.xlsx");
      sut.generateMemberListFileByName(onNameFile);
      Assertions.assertTrue(onNameFile.exists());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @BeforeAll
  static void setup() {
    MockProvider.mock(ExternalRelationPartnerRepository.class);
    MockProvider.mock(ExternalRelationClubRepository.class);
    MockProvider.mock(ExternalRelationOtherRepository.class);
    MockProvider.mock(InternalRelationRepository.class);
    memberRepository = MockProvider.mock(MemberRepository.class);
    List<Member> members = new ArrayList<>();
    members.add(member());
    when(memberRepository.findMembersByStatuses(statuses)).thenReturn(members);
    sut = ExportAddressService.getInstance();
  }


  private static Member member() {
    Member m = new Member();
    m.setMemberNumber(1234);
    Address a = m.getAddress();
    a.setStreet("straat");
    a.setAddressNumber("99");
    a.setPostalCode("1234 AA");
    a.setCity("Ons Dorp");
    m.setInitials("T.");
    m.setLastName("Tester");
    m.setEmail("tester@Test.nl");
    m.setIbanNumber("NL54ASNB0709093276");
    m.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    return m;
  }


}
