package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.excel.lid.CCNLLid;
import nl.ealse.ccnl.ledenadministratie.excelimport.ImportService.ProcessType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;

public class MemberImport {
  private final MemberRepository repository;

  private final ProcessType processType;

  private final List<Number> existingNumbers;

  public MemberImport(MemberRepository repository, ProcessType processType) {
    this.repository = repository;
    this.processType = processType;
    this.existingNumbers = repository.getAllMemberNumbers();
  }

  public void importMember(CCNLLid lid) {
    Optional<Member> optionalRelation = repository.findById(lid.getRelatienummer());
    Member member;
    if (optionalRelation.isEmpty()) {
      member = new Member();
      fillMember(lid, member);
      repository.save(member);
    } else if (processType != ProcessType.ADD) {
      member = optionalRelation.get();
      fillMember(lid, member);
      repository.save(member);
      if (processType == ProcessType.REPLACE) {
        existingNumbers.remove(indexOf(lid.getRelatienummer()));
      }
    }

  }

  public void lastYearMembership(CCNLLid lid) {
    Member updateMember = getMemberToUpdate(lid);
    updateMember.setMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP);
    repository.save(updateMember);
  }

  public void inactiveMembership(CCNLLid lid) {
    Member updateMember = getMemberToUpdate(lid);
    updateMember.setMemberStatus(MembershipStatus.INACTIVE);
    repository.save(updateMember);
  }

  public void overDueMembership(CCNLLid lid) {
    Member updateMember = getMemberToUpdate(lid);
    updateMember.setMemberStatus(MembershipStatus.OVERDUE);
    repository.save(updateMember);
  }

  public void addressInvalid(CCNLLid lid) {
    Member updateMember = getMemberToUpdate(lid);
    updateMember.setMemberInfo(lid.getOpmerking());
    updateMember.getAddress().setAddressInvalid(true);
    repository.save(updateMember);
  }

  public void finalizeImport() {
    if (processType == ProcessType.REPLACE) {
      for (Number nr : existingNumbers) {
        Integer id;
        if (nr instanceof Integer) {
          id = (Integer) nr;
        } else {
          id = Integer.valueOf(nr.intValue());
        }
        repository.deleteById(id);
      }

    }
  }


  private Member getMemberToUpdate(CCNLLid lid) {
    Member updateMember;
    Optional<Member> member = repository.findById(lid.getLidNummer());
    if (member.isPresent()) {
      updateMember = member.get();
    } else {
      updateMember = new Member();
      fillMember(lid, updateMember);
    }
    return updateMember;
  }

  private void fillMember(CCNLLid lid, Member member) {
    member.setAddress(AddressMapping.mapAddress(lid));
    member.setCurrentYearPaid(lid.isHeeftBetaald());
    member.setEmail(lid.getEmail());
    member.setIbanNumber(lid.getIbanNummer());
    member.setInitials(lid.getVoornaam());
    member.setLastName(lid.getAchternaam());
    member.setLastNamePrefix(lid.getTussenvoegsel());
    member.setMembercardIssued(lid.isPasVerstuurd());
    member.setMemberNumber(lid.getLidNummer());
    member.setMemberSince(DateUtil.toLocaleDate(lid.getLidVanaf()));
    member.setMemberStatus(MembershipStatus.ACTIVE);
    member.setPaymentDate(DateUtil.toLocaleDate(lid.getBetaaldatum()));
    if (lid.getIncassoAanduiding() == null) {
      member.setPaymentMethod(PaymentMethod.UNKNOWN);
    } else {
      switch (lid.getIncassoAanduiding()) {
        case "I":
          member.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
          break;
        case "A":
          member.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
          break;
        case "E":
          member.setPaymentMethod(PaymentMethod.NOT_APPLICABLE);
          break;
        default:
          member.setPaymentMethod(PaymentMethod.UNKNOWN);
      }
    }
    member.setTelephoneNumber(lid.getTelefoon());
  }

  private int indexOf(int relatienummer) {
    for (int ix = 0; ix < existingNumbers.size(); ix++) {
      if (existingNumbers.get(ix).intValue() == relatienummer) {
        return ix;
      }
    }
    return -1;
  }


}
