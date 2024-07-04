package nl.ealse.ccnl.ledenadministratie.excelimport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.dao.MemberRepository;
import nl.ealse.ccnl.ledenadministratie.dao.util.TransactionUtil;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLWorkbook;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetNotFoundException;
import nl.ealse.ccnl.ledenadministratie.excel.club.CCNLClubSheet;
import nl.ealse.ccnl.ledenadministratie.excel.intern.CCNLInternSheet;
import nl.ealse.ccnl.ledenadministratie.excel.lid.CCNLLidSheet;
import nl.ealse.ccnl.ledenadministratie.excel.partner.CCNLPartnerSheet;

public class ImportHandler {

  private final MemberRepository memberRepository;
  private final ExternalRelationClubRepository clubRepository;
  private final ExternalRelationOtherRepository otherRepository;
  private final ExternalRelationPartnerRepository partnerRepository;
  private final InternalRelationRepository internalRepository;

  public ImportHandler(MemberRepository memberRepository,
      ExternalRelationClubRepository clubRepository,
      ExternalRelationOtherRepository otherRepository,
      ExternalRelationPartnerRepository partnerRepository, InternalRelationRepository internalRepository) {
    this.memberRepository = memberRepository;
    this.clubRepository = clubRepository;
    this.otherRepository = otherRepository;
    this.partnerRepository = partnerRepository;
    this.internalRepository = internalRepository;
  }

  public void importFromExcel(CCNLWorkbook workbook, ImportSelection selection)
      throws SheetNotFoundException {
    ProcessType importType = selection.getImportType().getProcessType();
    TransactionUtil.inTransction(() -> {
      if (selection.isMembers()) {
        importMembersFromExcel(workbook, importType);
      }
      if (selection.isPartners()) {
        importPartnersFromExcel(workbook, importType);
      }
      if (selection.isClubs()) {
        importClubsFromExcel(workbook, importType);
      }
      if (selection.isExternal()) {
        importExternalRelationsFromExcel(workbook, importType);
      }
      if (selection.isInternal()) {
        importInternalRelationsFromExcel(workbook, importType);
      }
    });

  }

  private void importMembersFromExcel(CCNLWorkbook workbook, ProcessType importType)
      throws SheetNotFoundException {
    final MemberImport importHandler = new MemberImport(memberRepository, importType);
    boolean sheetFound = handleLedenSheet(workbook, SheetDefinition.LEDEN, importHandler);
    // The sequence of following actions is important.
    // If a member exists on multiple Excel-sheets then the
    // most relevant state must be handled last to reflect the correct state.
    sheetFound = handleLedenSheet(workbook, SheetDefinition.OPZEGGEN_VOLGEND_JAAR, importHandler)
        || sheetFound;
    sheetFound = handleLedenSheet(workbook, SheetDefinition.OPZEGGERS, importHandler) || sheetFound;
    sheetFound =
        handleLedenSheet(workbook, SheetDefinition.NIET_BETAALD, importHandler) || sheetFound;
    sheetFound = handleLedenSheet(workbook, SheetDefinition.RETOUR, importHandler) || sheetFound;

    if (sheetFound) {
      importHandler.finalizeImport();
    } else {
      throw new SheetNotFoundException(
          "In het Excel bestand geen tabbladnaam gevonden m.b.t. leden");
    }
  }

  private boolean handleLedenSheet(CCNLWorkbook workbook, SheetDefinition def,
      MemberImport importHandler) {
    try {
      CCNLLidSheet leden = workbook.getSheet(def, CCNLLidSheet.class);
      switch (def) {
        case LEDEN:
          leden.forEach(importHandler::importMember);
          break;
        case OPZEGGEN_VOLGEND_JAAR:
          leden.forEach(importHandler::lastYearMembership);
          break;
        case OPZEGGERS:
          leden.forEach(importHandler::inactiveMembership);
          break;
        case NIET_BETAALD:
          leden.forEach(importHandler::overDueMembership);
          break;
        case RETOUR:
          leden.forEach(importHandler::addressInvalid);
          break;
        default:
          break;
      }
      return true;
    } catch (SheetNotFoundException e) {
      return false;
    }

  }

  private void importPartnersFromExcel(CCNLWorkbook workbook, ProcessType importType)
      throws SheetNotFoundException {
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

  private void importClubsFromExcel(CCNLWorkbook workbook, ProcessType importType)
      throws SheetNotFoundException {
    CCNLClubSheet clubs = workbook.getSheet(SheetDefinition.CLUBS, CCNLClubSheet.class);
    final ExternalClubImport importHandler = new ExternalClubImport(clubRepository, importType);
    clubs.forEach(importHandler::importExternalRelation);
    importHandler.finalizeImport();
  }

  private void importExternalRelationsFromExcel(CCNLWorkbook workbook, ProcessType importType)
      throws SheetNotFoundException {
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

  private void importInternalRelationsFromExcel(CCNLWorkbook workbook, ProcessType importType)
      throws SheetNotFoundException {
    CCNLInternSheet functies = workbook.getSheet(SheetDefinition.INTERN, CCNLInternSheet.class);
    InternalRelationImport importHandler =
        new InternalRelationImport(internalRepository, importType);
    functies.forEach(importHandler::importInternalRelation);
    importHandler.finalizeImport();
  }

  @Getter
  @AllArgsConstructor
  public static class ImportSelection {

    private boolean members;
    private boolean partners;
    private boolean clubs;
    private boolean external;
    private boolean internal;

    private ImportType importType;

  }

}
