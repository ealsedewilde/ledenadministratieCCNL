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
    CCNLLidSheet leden = workbook.getSheet(SheetDefinition.LEDEN, CCNLLidSheet.class);

    // The sequence of following actions is important.
    // If a member exists on multiple Excel-sheets then the
    // most relevant state must be handled last to reflect the correct state.
    final MemberImport importHandler = new MemberImport(memberRepository, importType);
    leden.forEach(importHandler::importMember);
    leden = workbook.getSheet(SheetDefinition.OPZEGGEN_VOLGEND_JAAR, CCNLLidSheet.class);
    leden.forEach(importHandler::lastYearMembership);
    leden = workbook.getSheet(SheetDefinition.OPZEGGERS, CCNLLidSheet.class);
    leden.forEach(importHandler::inactiveMembership);
    leden = workbook.getSheet(SheetDefinition.NIET_BETAALD, CCNLLidSheet.class);
    leden.forEach(importHandler::overDueMembership);
    leden = workbook.getSheet(SheetDefinition.RETOUR, CCNLLidSheet.class);
    leden.forEach(importHandler::addressInvalid);
    importHandler.finalizeImport();
  }

  @Transactional
  public void importPartnersFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLPartnerSheet partners = workbook.getSheet(SheetDefinition.RELATIES, CCNLPartnerSheet.class);
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
    CCNLClubSheet clubs =  workbook.getSheet(SheetDefinition.CLUBS, CCNLClubSheet.class);
    final ExternalClubImport importHandler = new ExternalClubImport(clubRepository, importType);
    clubs.forEach(importHandler::importExternalRelation);
    importHandler.finalizeImport();
  }

  @Transactional
  public void importExternalRelationsFromExcel(CCNLWorkbook workbook, ProcessType importType) {
    CCNLPartnerSheet partners = workbook.getSheet(SheetDefinition.RELATIES, CCNLPartnerSheet.class);
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
    CCNLInternSheet functies = workbook.getSheet(SheetDefinition.INTERN, CCNLInternSheet.class);
    InternalRelationImport importHandler =
        new InternalRelationImport(internalRepository, importType);
    functies.forEach(importHandler::importInternalRelation);
    importHandler.finalizeImport();
  }


}
