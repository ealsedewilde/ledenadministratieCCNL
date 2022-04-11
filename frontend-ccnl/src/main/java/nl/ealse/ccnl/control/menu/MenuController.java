package nl.ealse.ccnl.control.menu;

import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class MenuController {

  private final ApplicationEventPublisher eventPublisher;

  private final PageController pageController;

  public MenuController(ApplicationEventPublisher eventPublisher, PageController pageController) {
    this.eventPublisher = eventPublisher;
    this.pageController = pageController;
  }

  // Member controls

  public void newMember() {
    pageController.setActivePage(PageName.MEMBER_PERSONAL);
    // pre load page so the related controller receives the event.
    pageController.loadPage(PageName.WELCOME_LETTER);
    eventPublisher.publishEvent(new MemberSeLectionEvent(this, MenuChoice.NEW_MEMBER));
  }

  public void amendMember() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.MEMBER_PERSONAL);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_MEMBER));
  }

  public void cancelMembership() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.MEMBER_CANCEL);
    pageController.loadPage(PageName.MEMBER_CANCEL_MAIL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.CANCEL_MEMBERSHIP));
  }

  public void paymentAuthorization() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.SEPA_AUTHORIZATION_ADD);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.PAYMENT_AUTHORIZATION));
  }

  // Partner controls
  public void newPartner() {
    pageController.setActivePage(PageName.PARTNER_PERSONAL);
    eventPublisher.publishEvent(new PartnerSelectionEvent(this, MenuChoice.NEW_PARTNER));
  }

  public void amendPartner() {
    pageController.setActivePage(PageName.PARTNER_SEARCH);
    pageController.loadPage(PageName.PARTNER_PERSONAL);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_PARTNER));
  }

  public void deletePartner() {
    pageController.setActivePage(PageName.PARTNER_SEARCH);
    pageController.loadPage(PageName.PARTNER_DELETE);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_PARTNER));
  }

  // Club controls
  public void newExternalClub() {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_PERSONAL);
    eventPublisher.publishEvent(new ExternalClubSelectionEvent(this, MenuChoice.NEW_EXTERNAL_CLUB));
  }

  public void amendExternalClub() {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_CLUB_PERSONAL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_CLUB));
  }

  public void deleteExternalClub() {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_CLUB_DELETE);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_CLUB));
  }

  // External Relation controls
  public void newExternalRelation() {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_PERSONAL);
    eventPublisher
        .publishEvent(new ExternalOtherSelectionEvent(this, MenuChoice.NEW_EXTERNAL_RELATION));
  }

  public void amendExternalRelation() {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_RELATION_PERSONAL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_RELATION));
  }

  public void deleteExternalRelation() {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_RELATION_DELETE);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_RELATION));
  }

  // Internal Relation controls
  public void newInternalRelation() {
    pageController.setActivePage(PageName.INTERNAL_RELATION_PERSONAL);
    eventPublisher
        .publishEvent(new InternalRelationSelectionEvent(this, MenuChoice.NEW_INTERNAL_RELATION));
  }

  public void amendInternalRelation() {
    pageController.setActivePage(PageName.INTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.INTERNAL_RELATION_PERSONAL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.AMEND_INTERNAL_RELATION));
  }

  public void deleteInternalRelation() {
    pageController.setActivePage(PageName.INTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.INTERNAL_RELATION_DELETE);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.DELETE_INTERNAL_RELATION));
  }

  // Magazine controls
  public void magazineAddressList() {
    pageController.setActivePage(PageName.MAGAZINE_ADDRESS_LIST);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MAGAZINE_ADDRESS_LIST));
  }

  public void cardAddressList() {
    pageController.loadPage(PageName.MAGAZINE_ADDRESS_LIST);
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.CARD_ADDRESS_LIST));
  }

  public void magazineInvalidAddress() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.MAGAZINE_INVALID_ADDRESS);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.MAGAZINE_INVALID_ADDRESS));
  }

  // Correspondence controls
  public void addDocument() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.ADD_DOCUMENT);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.ADD_DOCUMENT));

  }

  public void viewDocument() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.VIEW_DOCUMENTS);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.VIEW_DOCUMENT));

  }

  // Report controls
  public void excelAll() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ALL_DATA));
  }

  public void excelNew() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_NEW_MEMBERS));
  }

  public void excelCancelation() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.REPORT_CANCELLED_MEMBERS));
  }

  public void excelOverdue() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.REPORT_OVERDUE_MEMBERS));
  }

  public void excelArchive() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ARCHIVE));
  }

  // Yearly controls
  public void resetPayments() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RESET_PAYMENTS));
  }

  public void directDebits() {
    pageController.setActivePage(PageName.DIRECT_DEBITS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_DIRECT_DEBITS_FILE));
  }

  public void reconcilePayments() {
    pageController.setActivePage(PageName.RECONCILE_PAYMENTS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RECONCILE_PAYMENTS));
  }

  public void reminderReport() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_REPORT));
  }

  public void makeReminderLettersDD() {
    pageController.setActivePage(PageName.PAYMENT_REMINDER_LETTERS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_DD));
  }

  public void makeReminderLettersBT() {
    pageController.setActivePage(PageName.PAYMENT_REMINDER_LETTERS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_BT));
  }

  public void yearly() {
    pageController.setActivePage(PageName.ANNUAL_ROLLOVER);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.ANNUAL_ROLLOVER));
  }

  // Maintenance controls
  public void manageSepaAuthorizationForm() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_SEPA_FORM));
  }

  public void manageDocumentTemplates() {
    pageController.setActivePage(PageName.TEMPLATES_OVERVIEW);
    pageController.loadPage(PageName.MANAGE_TEMPLATE);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW));
  }

  public void backupDatabase() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_BACKUP_DATABASE));
  }

  public void restoreDatabase() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_RESTORE_DATABASE));
  }

  public void importFromExcel() {
    pageController.setActivePage(PageName.EXCEL_IMPORT);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.IMPORT_FROM_EXCEL));
  }

  public void settings() {
    pageController.setActivePage(PageName.SETTINGS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.SETTINGS));
  }

  public void manageArchive() {
    pageController.setActivePage(PageName.MANAGE_ARCHIVE);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_ARCHIVE));
  }

}
