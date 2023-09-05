package nl.ealse.ccnl.control.menu;

import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;

/**
 * Controller for all menu bar choices.
 */
@Controller
public class MenuController {

  private final ApplicationEventPublisher eventPublisher;

  private final PageController pageController;

  public MenuController(ApplicationEventPublisher eventPublisher, PageController pageController) {
    this.eventPublisher = eventPublisher;
    this.pageController = pageController;
  }

  // Member controls
  @FXML
  void newMember() {
    pageController.setActivePage(PageName.MEMBER_PERSONAL);
    // pre load page so the related controller receives the event.
    pageController.loadPage(PageName.WELCOME_LETTER);
    eventPublisher.publishEvent(new MemberSeLectionEvent(this, MenuChoice.NEW_MEMBER));
  }
  
  @FXML
  void amendMember() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.MEMBER_PERSONAL);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_MEMBER));
  }
  
  @FXML
  void cancelMembership() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.MEMBER_CANCEL);
    pageController.loadPage(PageName.MEMBER_CANCEL_MAIL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.CANCEL_MEMBERSHIP));
  }
  
  @FXML
  void paymentAuthorization() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.SEPA_AUTHORIZATION_ADD);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.PAYMENT_AUTHORIZATION));
  }

  // Partner controls
  @FXML  
  void newPartner() {
    pageController.setActivePage(PageName.PARTNER_PERSONAL);
    eventPublisher.publishEvent(new PartnerSelectionEvent(this, MenuChoice.NEW_PARTNER));
  }

  @FXML void amendPartner() {
    pageController.setActivePage(PageName.PARTNER_SEARCH);
    pageController.loadPage(PageName.PARTNER_PERSONAL);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_PARTNER));
  }

  @FXML 
  void deletePartner() {
    pageController.setActivePage(PageName.PARTNER_SEARCH);
    pageController.loadPage(PageName.PARTNER_DELETE);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_PARTNER));
  }

  // Club controls
  @FXML 
  void newExternalClub() {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_PERSONAL);
    eventPublisher.publishEvent(new ExternalClubSelectionEvent(this, MenuChoice.NEW_EXTERNAL_CLUB));
  }

  @FXML 
  void amendExternalClub() {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_CLUB_PERSONAL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_CLUB));
  }

  @FXML 
  void deleteExternalClub() {
    pageController.setActivePage(PageName.EXTERNAL_CLUB_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_CLUB_DELETE);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_CLUB));
  }

  // External Relation controls
  @FXML 
  void newExternalRelation() {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_PERSONAL);
    eventPublisher
        .publishEvent(new ExternalOtherSelectionEvent(this, MenuChoice.NEW_EXTERNAL_RELATION));
  }

  @FXML 
  void amendExternalRelation() {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_RELATION_PERSONAL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_RELATION));
  }

  @FXML 
  void deleteExternalRelation() {
    pageController.setActivePage(PageName.EXTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.EXTERNAL_RELATION_DELETE);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_RELATION));
  }

  // Internal Relation controls
  @FXML 
  void newInternalRelation() {
    pageController.setActivePage(PageName.INTERNAL_RELATION_PERSONAL);
    eventPublisher
        .publishEvent(new InternalRelationSelectionEvent(this, MenuChoice.NEW_INTERNAL_RELATION));
  }

  @FXML 
  void amendInternalRelation() {
    pageController.setActivePage(PageName.INTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.INTERNAL_RELATION_PERSONAL);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.AMEND_INTERNAL_RELATION));
  }

  @FXML 
  void deleteInternalRelation() {
    pageController.setActivePage(PageName.INTERNAL_RELATION_SEARCH);
    pageController.loadPage(PageName.INTERNAL_RELATION_DELETE);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.DELETE_INTERNAL_RELATION));
  }

  // Magazine controls
  @FXML 
  void magazineAddressList() {
    pageController.setActivePage(PageName.MAGAZINE_ADDRESS_LIST);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MAGAZINE_ADDRESS_LIST));
  }

  @FXML 
  void cardAddressList() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.CARD_ADDRESS_LIST));
  }

  @FXML 
  void memberListByNumber() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MEMBER_LIST_BY_NUMBER));
  }

  @FXML 
  void memberListByName() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MEMBER_LIST_BY_NAME));
  }

  @FXML 
  void magazineInvalidAddress() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.MAGAZINE_INVALID_ADDRESS);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.MAGAZINE_INVALID_ADDRESS));
  }

  // Correspondence controls
  @FXML 
  void addDocument() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.ADD_DOCUMENT);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.ADD_DOCUMENT));

  }

  @FXML 
  void viewDocument() {
    pageController.setActivePage(PageName.MEMBER_SEARCH);
    pageController.loadPage(PageName.VIEW_DOCUMENTS);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.VIEW_DOCUMENT));

  }

  // Report controls
  @FXML 
  void excelAll() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ALL_DATA));
  }

  @FXML 
  void excelNew() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_NEW_MEMBERS));
  }

  @FXML 
  void excelCancelation() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.REPORT_CANCELLED_MEMBERS));
  }

  @FXML 
  void excelOverdue() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(
        new MenuChoiceEvent(this, MenuChoice.REPORT_OVERDUE_MEMBERS));
  }

  @FXML 
  void excelArchive() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ARCHIVE));
  }

  // Yearly controls
  @FXML 
  void resetPayments() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RESET_PAYMENTS));
  }

  @FXML 
  void directDebits() {
    pageController.setActivePage(PageName.DIRECT_DEBITS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_DIRECT_DEBITS_FILE));
  }

  @FXML 
  void reconcilePayments() {
    pageController.setActivePage(PageName.RECONCILE_PAYMENTS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RECONCILE_PAYMENTS));
  }

  @FXML 
  void reminderReport() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_REPORT));
  }

  @FXML 
  void makeReminderLettersDD() {
    pageController.setActivePage(PageName.PAYMENT_REMINDER_LETTERS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_DD));
  }

  @FXML 
  void makeReminderLettersBT() {
    pageController.setActivePage(PageName.PAYMENT_REMINDER_LETTERS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_BT));
  }

  @FXML 
  void yearly() {
    pageController.setActivePage(PageName.ANNUAL_ROLLOVER);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.ANNUAL_ROLLOVER));
  }

  // Maintenance controls
  @FXML 
  void manageSepaAuthorizationForm() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_SEPA_FORM));
  }

  @FXML 
  void manageDocumentTemplates() {
    pageController.setActivePage(PageName.TEMPLATES_OVERVIEW);
    pageController.loadPage(PageName.MANAGE_TEMPLATE);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW));
  }

  @FXML 
  void backupDatabase() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_BACKUP_DATABASE));
  }

  @FXML 
  void restoreDatabase() {
    pageController.setActivePage(PageName.LOGO);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_RESTORE_DATABASE));
  }

  @FXML 
  void importFromExcel() {
    pageController.setActivePage(PageName.EXCEL_IMPORT);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.IMPORT_FROM_EXCEL));
  }

  @FXML 
  void settings() {
    pageController.setActivePage(PageName.SETTINGS);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.SETTINGS));
  }

  @FXML 
  void manageArchive() {
    pageController.setActivePage(PageName.MANAGE_ARCHIVE);
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_ARCHIVE));
  }

}
