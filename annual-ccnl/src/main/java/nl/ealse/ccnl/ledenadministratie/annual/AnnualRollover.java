package nl.ealse.ccnl.ledenadministratie.annual;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.dao.ArchiveRepository;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;
import nl.ealse.ccnl.ledenadministratie.model.ArchiveId;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;

/**
 * Rollover to a new membership year.
 *
 * @author ealse
 */
public class AnnualRollover {

  @Getter
  private static AnnualRollover instance = new AnnualRollover();
  private final ArchiveRepository archiveRepository = ArchiveRepository.getInstance();

  private static final EnumSet<MembershipStatus> statuses = EnumSet.of(MembershipStatus.INACTIVE,
      MembershipStatus.LAST_YEAR_MEMBERSHIP, MembershipStatus.OVERDUE);

  private final MemberRepository memberRepository;

  private AnnualRollover() {
    this.memberRepository = MemberRepository.getInstance();
  }
  /**
   * Rollover to a new membership year.
   * <p>
   * All non renewal members will be archived and removed from members database table.
   * </p>
   * <p>
   * The status is changed to OVERDUE for for all members that haven't paid yet.
   * </p>
   * <p>
   * The member card status is reset for all remaining members.
   * </p>
   */
  public void rollover() {
    TransactionUtil.inTransction(this::doRollover);
  }

  private void doRollover() {
    final List<Member> membersToRemove = memberRepository.findMembersByStatuses(statuses);
    final List<ArchivedMember> archiveList = new ArrayList<>();
    for (Iterator<Member> itr = membersToRemove.iterator(); itr.hasNext();) {
      Member member = itr.next();
      if (member.getMemberStatus() == MembershipStatus.LAST_YEAR_MEMBERSHIP
          && member.isCurrentYearPaid()) {
        // Current year paid indicates that the membership cancelation is for next year's rollover.
        // This is a very unusual situation
        itr.remove();
      } else {
        ArchiveId archiveId = new ArchiveId();
        archiveId.setArchiveYear(LocalDate.now().getYear());
        archiveId.setMemberNumber(member.getMemberNumber());
        ArchivedMember archivedMember = new ArchivedMember();
        archivedMember.setId(archiveId);
        archivedMember.setMember(member);
        archiveList.add(archivedMember);
      }
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
