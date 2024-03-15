package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;

public class MemberRepository extends BaseRepository<Member> {

  @Getter
  private static MemberRepository instance = new MemberRepository();

  private MemberRepository() {
    super(Member.class);
  }

  public List<Number> getAllMemberNumbers() {
    TypedQuery<Number> query =
        getEntityManager().createQuery("SELECT m.memberNumber FROM Member m", Number.class);
    return query.getResultList();
  }

  public List<Member> findMembersCurrentYearNotPaid(Set<MembershipStatus> statuses,
      Set<PaymentMethod> paymentMethods) {
    return executeQuery("SELECT M FROM Member M WHERE M.currentYearPaid = FALSE AND "
        + "M.memberStatus IN ?1 AND " + "M.paymentMethod IN ?2 ORDER BY M.memberNumber",
        statuses, paymentMethods);
  }

  public List<Member> findMemberByMemberStatus(MembershipStatus status) {
    return executeQuery("SELECT M FROM Member M WHERE M.memberStatus IN ?1", status);
  }

  public List<Member> findMembersByStatuses(Set<MembershipStatus> statuses) {
    return executeQuery("SELECT M FROM Member M WHERE M.memberStatus IN ?1 ORDER BY M.memberNumber",
        statuses);
  }

  public List<Member> findMemberByPaymentMethodAndMemberStatusAndCurrentYearPaidOrderByMemberNumber(
      PaymentMethod paymentMethod, MembershipStatus status, boolean currentYearPaid) {
    return executeQuery(
        "SELECT M FROM Member M WHERE M.paymentMethod = ?1 AND M.memberStatus = ?2 AND M.currentYearPaid = ?3",
        paymentMethod, status, currentYearPaid);
  }

  public List<Member> findMembersByPaymentMethods(Set<PaymentMethod> paymentMethods) {
    return executeQuery("SELECT M FROM Member M WHERE M.paymentMethod IN ?1", paymentMethods);
  }

  public List<Member> findNewMembers(LocalDate refDate) {
    return executeQuery("Select M from Member M where M.memberSince >= ?1", refDate);
  }

  public List<Member> findMembersByStatusesOrderByName(Set<MembershipStatus> statuses) {
    return executeQuery("SELECT M FROM Member M WHERE M.memberStatus IN ?1 ORDER BY M.lastName",
        statuses);
  }

  // Start search queries

  public List<Member> findMembersByAddress(String searchValue) {
    return executeQuery(
        "SELECT M FROM Member M WHERE LOWER(M.address.street) LIKE LOWER(concat(?1, '%'))",
        searchValue);
  }

  public List<Member> findMembersByCity(String searchValue) {
    return executeQuery(
        "SELECT M FROM Member M WHERE LOWER(M.address.city) LIKE LOWER(concat(?1, '%'))",
        searchValue);
  }

  public List<Member> findMembersByName(String searchValue) {
    return executeQuery(
        "SELECT M FROM Member M WHERE LOWER(M.lastName) LIKE LOWER(concat(?1, '%'))", searchValue);
  }

  public List<Member> findMembersByPostalCode(String searchValue) {
    return executeQuery(
        "SELECT M FROM Member M WHERE LOWER(M.address.postalCode) LIKE LOWER(concat(?1, '%'))",
        searchValue);
  }

  @Override
  protected Object getPrimaryKey(Member entity) {
    return entity.getMemberNumber();
  }

}
