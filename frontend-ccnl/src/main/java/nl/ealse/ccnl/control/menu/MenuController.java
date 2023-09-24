package nl.ealse.ccnl.control.menu;

import javafx.fxml.FXML;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

/**
 * Controller for all menu bar choices.
 */
@Controller
public class MenuController {

  private final ApplicationEventPublisher eventPublisher;

  public MenuController(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  // Member controls
  @FXML
  void newMember() {
    eventPublisher.publishEvent(new MemberSeLectionEvent(this, MenuChoice.NEW_MEMBER));
  }

  @FXML
  void amendMember() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_MEMBER));
  }

  @FXML
  void cancelMembership() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.CANCEL_MEMBERSHIP));
  }

  @FXML
  void paymentAuthorization() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PAYMENT_AUTHORIZATION));
  }

  // Partner controls
  @FXML
  void newPartner() {
    eventPublisher.publishEvent(new PartnerSelectionEvent(this, MenuChoice.NEW_PARTNER));
  }

  @FXML
  void amendPartner() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_PARTNER));
  }

  @FXML
  void deletePartner() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_PARTNER));
  }

  // Club controls
  @FXML
  void newExternalClub() {
    eventPublisher.publishEvent(new ExternalClubSelectionEvent(this, MenuChoice.NEW_EXTERNAL_CLUB));
  }

  @FXML
  void amendExternalClub() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_CLUB));
  }

  @FXML
  void deleteExternalClub() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_CLUB));
  }

  // External Relation controls
  @FXML
  void newExternalRelation() {
    eventPublisher
        .publishEvent(new ExternalOtherSelectionEvent(this, MenuChoice.NEW_EXTERNAL_RELATION));
  }

  @FXML
  void amendExternalRelation() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_RELATION));
  }

  @FXML
  void deleteExternalRelation() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_RELATION));
  }

  // Internal Relation controls
  @FXML
  void newInternalRelation() {
    eventPublisher
        .publishEvent(new InternalRelationSelectionEvent(this, MenuChoice.NEW_INTERNAL_RELATION));
  }

  @FXML
  void amendInternalRelation() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_INTERNAL_RELATION));
  }

  @FXML
  void deleteInternalRelation() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_INTERNAL_RELATION));
  }

  // Magazine controls
  @FXML
  void magazineAddressList() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MAGAZINE_ADDRESS_LIST));
  }

  @FXML
  void cardAddressList() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.CARD_ADDRESS_LIST));
  }

  @FXML
  void memberListByNumber() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MEMBER_LIST_BY_NUMBER));
  }

  @FXML
  void memberListByName() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MEMBER_LIST_BY_NAME));
  }

  @FXML
  void magazineInvalidAddress() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MAGAZINE_INVALID_ADDRESS));
  }

  // Correspondence controls
  @FXML
  void addDocument() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.ADD_DOCUMENT));

  }

  @FXML
  void viewDocument() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.VIEW_DOCUMENT));

  }

  // Report controls
  @FXML
  void excelAll() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ALL_DATA));
  }

  @FXML
  void excelNew() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_NEW_MEMBERS));
  }

  @FXML
  void excelCancelation() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_CANCELLED_MEMBERS));
  }

  @FXML
  void excelOverdue() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_OVERDUE_MEMBERS));
  }

  @FXML
  void excelArchive() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ARCHIVE));
  }

  // Yearly controls
  @FXML
  void resetPayments() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RESET_PAYMENTS));
  }

  @FXML
  void directDebits() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_DIRECT_DEBITS_FILE));
  }

  @FXML
  void reconcilePayments() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RECONCILE_PAYMENTS));
  }

  @FXML
  void reminderReport() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_REPORT));
  }

  @FXML
  void makeReminderLettersDD() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_DD));
  }

  @FXML
  void makeReminderLettersBT() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_BT));
  }

  @FXML
  void yearly() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.ANNUAL_ROLLOVER));
  }

  // Maintenance controls
  @FXML
  void manageSepaAuthorizationForm() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_SEPA_FORM));
  }

  @FXML
  void manageDocumentTemplates() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW));
  }

  @FXML
  void backupDatabase() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_BACKUP_DATABASE));
  }

  @FXML
  void restoreDatabase() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_RESTORE_DATABASE));
  }

  @FXML
  void importFromExcel() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.IMPORT_FROM_EXCEL));
  }

  @FXML
  void settings() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.SETTINGS));
  }

  @FXML
  void manageArchive() {
    eventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_ARCHIVE));
  }

}
