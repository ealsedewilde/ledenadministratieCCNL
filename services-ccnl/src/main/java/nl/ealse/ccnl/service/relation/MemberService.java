package nl.ealse.ccnl.service.relation;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.util.MemberNumberFactory;

@Slf4j
@AllArgsConstructor
public class MemberService {
  {log.info("Service created");}

  private final MemberRepository dao;
  private final MemberNumberFactory numberFactory;

  public Integer getFreeNumber() {
    return numberFactory.getNewNumber();
  }

  public List<Member> searchMemberWithoutSepa(SearchItem searchItem, String searchValue) {
    List<Member> members = searchMember(searchItem, searchValue);
    return members.stream()
        .filter(member -> member.getDocuments().stream()
            .filter(doc -> doc.getDocumentType() == DocumentType.SEPA_AUTHORIZATION).count() == 0)
        .toList();
  }

  public List<Member> searchMember(SearchItem searchItem, String searchValue) {
    List<Member> result = new ArrayList<>();
    switch (searchItem) {
      case CITY:
        result.addAll(dao.findMembersByCity(searchValue));
        break;
      case NAME:
        result.addAll(dao.findMembersByName(searchValue));
        break;
      case NUMBER:
        Integer id = Integer.parseInt(searchValue);
        Optional<Member> m = dao.findById(id);
        if (m.isPresent()) {
          result.add(m.get());
        }
        break;
      case POSTAL_CODE:
        result.addAll(dao.findMembersByPostalCode(searchValue));
        break;
      case STREET:
        result.addAll(dao.findMembersByAddress(searchValue));
        break;
      default:
        break;

    }
    return result;
  }

  public Optional<Member> readMember(Integer memberNumber) {
    return dao.findById(memberNumber);
  }

  public void save(Member member) {
    dao.save(member);
  }

  /**
   * Find members with valid address whom pay not enough.
   *
   * @return list of non paying members with valid address
   */
  public List<Member> findMembersCurrentYearPartlyPaid() {
    return findMembers(null);
  }

  /**
   * Find not paying members with valid address.
   *
   * @param paymentMethod - payment method for a member
   * @return list of non paying members with valid address
   */
  public List<Member> findMembersCurrentYearNotPaid(PaymentMethod paymentMethod) {
    return findMembers(paymentMethod);
  }
  
  private List<Member> findMembers(PaymentMethod paymentMethod) {
    EnumSet<MembershipStatus> statuses =
        EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.AFTER_APRIL);
    if (paymentMethod == null) {
      return dao.findMembersCurrentYearPartlyPaidLetters(statuses);
    }
    EnumSet<PaymentMethod> paymentMethods = EnumSet.of(paymentMethod);
    return dao.findMembersCurrentYearNotPaidLetters(statuses, paymentMethods);
  }

}
