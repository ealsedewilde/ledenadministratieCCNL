package nl.ealse.ccnl.ledenadministratie.excelimport;

import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.club.CCNLClubSheet;
import nl.ealse.ccnl.ledenadministratie.excel.intern.CCNLInternSheet;
import nl.ealse.ccnl.ledenadministratie.excel.lid.CCNLLidSheet;
import nl.ealse.ccnl.ledenadministratie.excel.partner.CCNLPartnerSheet;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ImportHandler {

  private final MemberRepository memberRepository;
  private final ExternalRelationClubRepository clubRepository;
  private final ExternalRelationOtherRepository otherRepository;
  private final ExternalRelationPartnerRepository partnerRepository;
  private final InternalRelationRepository internalRepository;

  public ImportHandler(MemberRepository memberRepository,
      ExternalRelationClubRepository clubRepository, InternalRelationRepository internalRepository,
      ExternalRelationOtherRepository otherRepository,
      ExternalRelationPartnerRepository partnerRepository) {
    this.memberRepository = memberRepository;
    this.clubRepository = clubRepository;
    this.otherRepository = otherRepository;
    this.partnerRepository = partnerRepository;
    this.internalRepository = internalRepository;
  }

  @Transactional
  public void importMembersFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLLidSheet leden = (CCNLLidSheet) workbook.getSheet(SheetDefinition.LEDEN);

    // The sequence of following actions is important.
    // If a member exists on multiple Excel-sheets then the
    // most relevant state must be handled last to reflect the correct state.
    final MemberImport importHandler = new MemberImport(memberRepository, importType);
    leden.forEach(importHandler::importMember);
    leden = (CCNLLidSheet) workbook.getSheet(SheetDefinition.OPZEGGEN_VOLGEND_JAAR);
    leden.forEach(importHandler::lastYearMembership);
    leden = (CCNLLidSheet) workbook.getSheet(SheetDefinition.OPZEGGERS);
    leden.forEach(importHandler::inactiveMembership);
    leden = (CCNLLidSheet) workbook.getSheet(SheetDefinition.NIET_BETAALD);
    leden.forEach(importHandler::overDueMembership);
    leden = (CCNLLidSheet) workbook.getSheet(SheetDefinition.RETOUR);
    leden.forEach(importHandler::addressInvalid);
    importHandler.finalizeImport();
  }

  @Transactional
  public void importPartnersFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLPartnerSheet partners = (CCNLPartnerSheet) workbook.getSheet(SheetDefinition.RELATIES);
    final CommercialPartnerImport importHandler =
        new CommercialPartnerImport(partnerRepository, importType);
    partners.forEach(partner -> {
      if (partner.getPartnerNummer() >= 8500) {
        importHandler.importExternalRelation(partner);
      }
    });
    importHandler.finalizeImport();
  }

  @Transactional
  public void importClubsFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLClubSheet partners = (CCNLClubSheet) workbook.getSheet(SheetDefinition.CLUBS);
    final ExternalClubImport importHandler = new ExternalClubImport(clubRepository, importType);
    partners.forEach(importHandler::importExternalRelation);
    importHandler.finalizeImport();
  }

  @Transactional
  public void importExternalRelationsFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLPartnerSheet partners = (CCNLPartnerSheet) workbook.getSheet(SheetDefinition.RELATIES);
    final OtherExternalRelationImport importHandler =
        new OtherExternalRelationImport(otherRepository, importType);
    partners.forEach(partner -> {
      if (partner.getPartnerNummer() < 8500) {
        importHandler.importExternalRelation(partner);
      }
    });
    importHandler.finalizeImport();
  }

  @Transactional
  public void importInternalRelationsFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLInternSheet functies = (CCNLInternSheet) workbook.getSheet(SheetDefinition.INTERN);
    InternalRelationImport importHandler =
        new InternalRelationImport(internalRepository, importType);
    functies.forEach(importHandler::importInternalRelation);
    importHandler.finalizeImport();
  }


}
