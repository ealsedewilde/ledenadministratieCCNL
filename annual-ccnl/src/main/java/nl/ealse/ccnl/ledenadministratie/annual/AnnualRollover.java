package nl.ealse.ccnl.ledenadministratie.annual;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ArchiveId;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.ArchiveRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rollover to a new membership year.
 * @author ealse
 */
@Component
public class AnnualRollover {

  private final ArchiveRepository archiveRepository;

  private final MemberRepository memberRepository;

  private final EnumSet<MembershipStatus> statuses = EnumSet.of(MembershipStatus.INACTIVE,
      MembershipStatus.LAST_YEAR_MEMBERSHIP, MembershipStatus.OVERDUE);

  public AnnualRollover(ArchiveRepository archiveRepository, MemberRepository memberRepository) {
    this.archiveRepository = archiveRepository;
    this.memberRepository = memberRepository;
  }

  /**
   * Rollover to a new membership year. 
   * <p>
   * All non renewal members will be archived and removed from
   * members database table. 
   * </p>
   * <p>
   * The status is changed to OVERDUE for for all members that haven't paid
   * yet.
   * </p> 
   * <p>
   * The member card status is reset for all remaining members.
   * </p>
   */
  @Transactional
  public void rollover() {
    final List<Member> membersToRemove = memberRepository.findMembersByStatuses(statuses);
    final List<ArchivedMember> archiveList = new ArrayList<>();
    for (Member member : membersToRemove) {
      ArchiveId archiveId = new ArchiveId();
      archiveId.setArchiveYear(LocalDate.now().getYear());
      archiveId.setMemberNumber(member.getMemberNumber());
      ArchivedMember archivedMember = new ArchivedMember();
      archivedMember.setId(archiveId);
      archivedMember.setMember(member);
      archiveList.add(archivedMember);
    }
    archiveRepository.saveAll(archiveList);
    memberRepository.deleteAll(membersToRemove);

    List<Member> members = memberRepository.findAll();
    for (Member member : members) {
      if (member.getPaymentMethod() == PaymentMethod.NOT_APPLICABLE) {
        member.setCurrentYearPaid(true);
        member.setPaymentDate(LocalDate.now());
      } else if (!member.isCurrentYearPaid()) {
        member.setMemberStatus(MembershipStatus.OVERDUE);
      }
    }

  }

}
