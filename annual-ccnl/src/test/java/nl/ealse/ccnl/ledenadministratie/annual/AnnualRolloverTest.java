package nl.ealse.ccnl.ledenadministratie.annual;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.dao.ArchiveRepository;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.test.MockProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class AnnualRolloverTest {
  
  private final EnumSet<MembershipStatus> statuses = EnumSet.of(MembershipStatus.INACTIVE,
      MembershipStatus.LAST_YEAR_MEMBERSHIP, MembershipStatus.OVERDUE);
  
  private AnnualRollover sut;
  
  private ArchiveRepository archiveRepository;
  
  private MemberRepository memberRepository;
  
  private int n = 1234;
  
  private List<Member> members;
  
  @Test
  void rollover() {
    sut = AnnualRollover.getInstance();
    sut.rollover();
    verify(archiveRepository).saveAll(ArgumentMatchers.<List<ArchivedMember>>any());
    verify(memberRepository).deleteAll(ArgumentMatchers.<List<Member>>any());
    Member m = members.get(0);
    Assertions.assertTrue(m.isCurrentYearPaid());
    m = members.get(1);
    Assertions.assertEquals(MembershipStatus.OVERDUE, m.getMemberStatus());

  }
  
  @BeforeEach
  void setup() {
    archiveRepository = MockProvider.mock(ArchiveRepository.class);
    memberRepository = MockProvider.mock(MemberRepository.class);
    members = new ArrayList<>();
    members.add(member(PaymentMethod.NOT_APPLICABLE));
    members.add(member(PaymentMethod.BANK_TRANSFER));
    when(memberRepository.findMembersByStatuses(statuses)).thenReturn(members);
    when(memberRepository.findAll()).thenReturn(members);
  }
  
  private Member member(PaymentMethod pm) {
    Member m = new Member();
    m.setMemberNumber(n++);
    m.setPaymentMethod(pm);
    return m;
  }

}
