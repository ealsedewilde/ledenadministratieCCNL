package nl.ealse.ccnl.service.relation;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.util.MemberNumberFactory;

@Slf4j
public class MemberService {
  
  @Getter
  private static MemberService instance = new MemberService();

  private final MemberRepository dao;

  private MemberService() {
    log.info("Service created");
    this.dao = MemberRepository.getInstance();
  }

  public Integer getFreeNumber() {
    return MemberNumberFactory.getInstance().getNewNumber();
  }

  @Transactional
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

  public List<Member> findMembersCurrentYearNotPaid(PaymentMethod paymentMethod) {
    return dao.findMembersCurrentYearNotPaid(EnumSet.of(paymentMethod));
  }

}
