package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

  @Query("Select m.memberNumber from Member m")
  List<Number> getAllMemberNumbers();

  @Query("SELECT M FROM Member M WHERE M.currentYearPaid = FALSE AND "
      + "M.memberStatus = nl.ealse.ccnl.ledenadministratie.model.MembershipStatus.ACTIVE AND "
      + "M.paymentMethod IN ?1 ORDER BY M.memberNumber")
  List<Member> findMembersCurrentYearNotPaid(Set<PaymentMethod> paymentMethods);

  List<Member> findMemberByMemberStatus(MembershipStatus status);

  @Query("SELECT M FROM Member M WHERE M.memberStatus IN ?1 ORDER BY M.memberNumber")
  List<Member> findMembersByStatuses(Set<MembershipStatus> statuses);

  List<Member> findMemberByPaymentMethodAndMemberStatusAndCurrentYearPaidOrderByMemberNumber(
      PaymentMethod paymentMethod, MembershipStatus status, boolean currentYearPaid);

  @Query("SELECT M FROM Member M WHERE M.paymentMethod IN ?1")
  List<Member> findMembersByPaymentMethods(Set<PaymentMethod> paymentMethods);


  @Query("Select M from Member M where M.memberSince >= ?1")
  List<Member> findNewMembers(LocalDate refDate);

  // Start search queries

  @Query("SELECT M FROM Member M WHERE LOWER(M.address.address) LIKE LOWER(concat(?1, '%'))")
  List<Member> findMembersByAddress(String searchValue);

  @Query("SELECT M FROM Member M WHERE LOWER(M.address.city) LIKE LOWER(concat(?1, '%'))")
  List<Member> findMembersByCity(String searchValue);

  @Query("SELECT M FROM Member M WHERE LOWER(M.lastName) LIKE LOWER(concat(?1, '%'))")
  List<Member> findMembersByName(String searchValue);

  @Query("SELECT M FROM Member M WHERE LOWER(M.address.postalCode) = LOWER(?1)")
  List<Member> findMembersByPostalCode(String searchValue);
}
