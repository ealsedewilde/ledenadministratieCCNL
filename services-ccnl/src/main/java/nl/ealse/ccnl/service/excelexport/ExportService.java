package nl.ealse.ccnl.service.excelexport;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.excel.Ledenbestand;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;

/**
 * Export all data to Excel.
 *
 * @author ealse
 *
 */
@Slf4j
public class ExportService {

  @Getter
  private static ExportService instance = new ExportService();

  private final ExternalRelationPartnerRepository commercialPartnerRepository;
  private final ExternalRelationClubRepository externalRelationClubRepository;
  private final ExternalRelationOtherRepository externalRelationOtherRepository;
  private final InternalRelationRepository internalRelationRepository;
  private final MemberRepository memberRepository;

  private ExportService() {
    log.info("Service created");
    this.commercialPartnerRepository = ExternalRelationPartnerRepository.getInstance();
    this.externalRelationClubRepository = ExternalRelationClubRepository.getInstance();
    this.externalRelationOtherRepository = ExternalRelationOtherRepository.getInstance();
    this.internalRelationRepository = InternalRelationRepository.getInstance();
    this.memberRepository = MemberRepository.getInstance();
  }

  /**
   * Export all data to an Excel file on the local filesystem.
   *
   * @param selectedFile - location for the target Excel file
   * @throws IOException in case generating the file fails
   */
  public void exportALL(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile)) {
      targetFile.addMemberHeading();
      List<Member> activeMembers =
          memberRepository.findMembersByStatuses(EnumSet.of(MembershipStatus.ACTIVE,
              MembershipStatus.LAST_YEAR_MEMBERSHIP, MembershipStatus.AFTER_APRIL));
      activeMembers.forEach(member -> {
        if (!member.getAddress().isAddressInvalid()) {
          targetFile.addMember(member);
        }
      });

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
      activeMembers.forEach(member -> {
        if (member.getAddress().isAddressInvalid()) {
          targetFile.addMember(member);
        }
      });

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
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile)) {
      targetFile.addMemberHeading();
      LocalDate refDate = LocalDate.now();
      refDate = refDate.withDayOfYear(1);
      memberRepository.findNewMembers(refDate).forEach(targetFile::addMember);
    }
  }

  public void exportCancelled(File selectedFile) throws IOException {
    makeFile(selectedFile, MembershipStatus.LAST_YEAR_MEMBERSHIP);
  }

  public void exportOverdue(File selectedFile) throws IOException {
    makeFile(selectedFile, MembershipStatus.OVERDUE);
  }

  public void exportAfterApril(File selectedFile) throws IOException {
    makeFile(selectedFile, MembershipStatus.AFTER_APRIL);
  }

  private void makeFile(File selectedFile, MembershipStatus status) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile)) {
      targetFile.addMemberHeading();
      List<Member> lastYearMembers = memberRepository.findMemberByMemberStatus(status);
      lastYearMembers.forEach(targetFile::addMember);
    }
  }

  public void paymentReminderReport(File selectedFile) throws IOException {
    try (Ledenbestand targetFile = new Ledenbestand(selectedFile)) {
      targetFile.addMemberHeading();
      EnumSet<MembershipStatus> statuses =
          EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.AFTER_APRIL);
      EnumSet<PaymentMethod> paymentMethods =
          EnumSet.of(PaymentMethod.BANK_TRANSFER, PaymentMethod.DIRECT_DEBIT);
      List<Member> selectedMembers =
          memberRepository.findMembersCurrentYearNotPaid(statuses, paymentMethods);
      selectedMembers.forEach(targetFile::addMember);
    }
  }

}
