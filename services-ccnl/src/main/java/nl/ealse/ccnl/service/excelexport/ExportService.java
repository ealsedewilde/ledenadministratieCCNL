package nl.ealse.ccnl.service.excelexport;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.Ledenbestand;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.springframework.stereotype.Service;

/**
 * Export all data to Excel.
 * 
 * @author ealse
 *
 */
@Service
@Slf4j
public class ExportService {

  private final ExternalRelationPartnerRepository commercialPartnerRepository;
  private final ExternalRelationClubRepository externalRelationClubRepository;
  private final ExternalRelationOtherRepository externalRelationOtherRepository;
  private final InternalRelationRepository internalRelationRepository;
  private final MemberRepository memberRepository;
  private final CCNLColumnProperties properties;

  public ExportService(MemberRepository memberRepository,
      ExternalRelationPartnerRepository commercialPartnerRepository,
      InternalRelationRepository internalRelationRepository,
      ExternalRelationClubRepository externalRelationClubRepository,
      ExternalRelationOtherRepository externalRelationOtherRepository,
      CCNLColumnProperties properties) {
    log.info("Service created");
    this.commercialPartnerRepository = commercialPartnerRepository;
    this.externalRelationClubRepository = externalRelationClubRepository;
    this.externalRelationOtherRepository = externalRelationOtherRepository;
    this.internalRelationRepository = internalRelationRepository;
    this.memberRepository = memberRepository;
    this.properties = properties;
  }

  /**
   * Export all data to an Excel file on the local filesystem.
   * 
   * @param selectedFile - location for the target Excel file
   * @throws IOException in case generating the file fails
   */
  public void exportALL(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile, properties)) {
      targetFile.addMemberHeading();
      List<Member> activeMembers = memberRepository.findMembersByStatuses(
          EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP));
      activeMembers.forEach(targetFile::addMember);

      targetFile.addSheet(SheetDefinition.NIEUWE_LEDEN);
      targetFile.addMemberHeading();
      LocalDate refDate = LocalDate.now();
      refDate = refDate.withDayOfYear(1);
      memberRepository.findNewMembers(refDate).forEach(targetFile::addMember);

      targetFile.addSheet(SheetDefinition.CLUBS);
      targetFile.addClubHeading();
      externalRelationClubRepository.findAll().forEach(targetFile::addClub);

      targetFile.addSheet(SheetDefinition.RELATIES);
      targetFile.addExternalRelationHeading();
      externalRelationOtherRepository.findAll().forEach(targetFile::addExternalRelation);
      commercialPartnerRepository.findAll().forEach(targetFile::addExternalRelation);

      targetFile.addSheet(SheetDefinition.INTERN);
      targetFile.addInternalRelationHeading();
      internalRelationRepository.findAll().forEach(targetFile::addInternalRelation);

      targetFile.addSheet(SheetDefinition.OPZEGGERS);
      targetFile.addMemberHeading();
      memberRepository.findMemberByMemberStatus(MembershipStatus.INACTIVE)
          .forEach(targetFile::addMember);

      targetFile.addSheet(SheetDefinition.RETOUR);
      targetFile.addMemberHeading();
      memberRepository.findMemberByMemberStatus(MembershipStatus.ACTIVE)
          .forEach(targetFile::addInvalidAddressMember);

      targetFile.addSheet(SheetDefinition.NIET_BETAALD);
      targetFile.addMemberHeading();
      memberRepository.findMemberByMemberStatus(MembershipStatus.OVERDUE)
          .forEach(targetFile::addMember);

      targetFile.addSheet(SheetDefinition.DUBBEL_BETAALD);
      targetFile.addMemberHeading();

      targetFile.addSheet(SheetDefinition.VREEMD);
      targetFile.addMemberHeading();

      targetFile.addSheet(SheetDefinition.OPZEGGEN_VOLGEND_JAAR);
      targetFile.addMemberHeading();
      List<Member> lastYearMembers =
          memberRepository.findMemberByMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP);
      lastYearMembers.forEach(targetFile::addMember);
    }
  }

  public void exportNew(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile, properties)) {
      targetFile.addMemberHeading();
      LocalDate refDate = LocalDate.now();
      refDate = refDate.withDayOfYear(1);
      memberRepository.findNewMembers(refDate).forEach(targetFile::addMember);
    }
  }

  public void exportCancelled(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile, properties)) {
      targetFile.addMemberHeading();
      List<Member> lastYearMembers =
          memberRepository.findMemberByMemberStatus(MembershipStatus.LAST_YEAR_MEMBERSHIP);
      lastYearMembers.forEach(targetFile::addMember);
    }
  }

  public void exportOverdue(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile, properties)) {
      targetFile.addMemberHeading();
      List<Member> lastYearMembers =
          memberRepository.findMemberByMemberStatus(MembershipStatus.OVERDUE);
      lastYearMembers.forEach(targetFile::addMember);
    }
  }

  public void paymentReminderReport(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile, properties)) {
      targetFile.addMemberHeading();
      EnumSet<PaymentMethod> paymentMethods = EnumSet.of(PaymentMethod.BANK_TRANSFER, PaymentMethod.DIRECT_DEBIT);
      List<Member> selectedMembers = memberRepository.findMembersCurrentYearNotPaid(paymentMethods);
      selectedMembers.forEach(targetFile::addMember);
    }
  }

}
