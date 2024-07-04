package nl.ealse.ccnl.service.relation;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.util.MemberNumberFactory;

@Slf4j
public class MemberService {

  private final MemberRepository dao;
  private final MemberNumberFactory numberFactory;

  public MemberService(MemberRepository dao, MemberNumberFactory numberFactory) {
    log.info("Service created");
    this.dao = dao;
    this.numberFactory= numberFactory;
  }

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
   * Find not paying members with valid address.
   *
   * @param paymentMethod - payment method for a member
   * @return list of non paying members with valid address
   */
  public List<Member> findMembersCurrentYearNotPaid(PaymentMethod paymentMethod) {
    EnumSet<MembershipStatus> statuses =
        EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.AFTER_APRIL);
    return dao.findMembersCurrentYearNotPaidLetters(statuses, EnumSet.of(paymentMethod));
  }

}
