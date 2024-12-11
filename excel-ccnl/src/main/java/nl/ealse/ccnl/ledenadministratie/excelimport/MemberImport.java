package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.excel.lid.CCNLLid;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;

public class MemberImport extends BaseImport<Member, CCNLLid> {
  private final MemberRepository repository;

  public MemberImport(MemberRepository repository, ProcessType processType) {
    super(repository, processType);
    this.repository = repository;
    setExistingNumbers(repository.getAllMemberNumbers());
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

  private Member getMemberToUpdate(CCNLLid lid) {
    Member updateMember;
    Optional<Member> member = repository.findById(lid.getLidNummer());
    if (member.isPresent()) {
      updateMember = member.get();
    } else {
      updateMember = new Member();
      fillRelation(lid, updateMember);
    }
    return updateMember;
  }

  @Override
  protected Member newInstance() {
    return new Member();
  }

  @Override
  public void fillRelation(CCNLLid lid, Member member) {
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
    if (lid.isAutomatischeIncasso()) {
      member.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    } else if (lid.isOverschrijving()) {
      member.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
    } else if (lid.isErelid()) {
      member.setPaymentMethod(PaymentMethod.NOT_APPLICABLE);
    } else {
      member.setPaymentMethod(PaymentMethod.UNKNOWN);
    }
    member.setTelephoneNumber(lid.getTelefoon());
    
  }
  @Override
  protected boolean validRelationNumber(int relatienummer) {
    return relatienummer > 0 && relatienummer < 5000;
  }

}
