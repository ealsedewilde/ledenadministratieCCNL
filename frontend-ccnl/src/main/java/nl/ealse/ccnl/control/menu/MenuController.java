package nl.ealse.ccnl.control.menu;

import javafx.fxml.FXML;
import lombok.Getter;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;

/**
 * Controller for all menu bar choices.
 */
public class MenuController {
  
  @Getter
  private static final MenuController instance = new MenuController();
  
  private MenuController() {}

  // Member controls
  @FXML
  void newMember() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.NEW_MEMBER));
  }

  @FXML
  void amendMember() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_MEMBER));
  }

  @FXML
  void cancelMembership() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.CANCEL_MEMBERSHIP));
  }

  @FXML
  void paymentAuthorization() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PAYMENT_AUTHORIZATION));
  }

  // Partner controls
  @FXML
  void newPartner() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.NEW_PARTNER));
  }

  @FXML
  void amendPartner() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_PARTNER));
  }

  @FXML
  void deletePartner() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_PARTNER));
  }

  // Club controls
  @FXML
  void newExternalClub() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.NEW_EXTERNAL_CLUB));
  }

  @FXML
  void amendExternalClub() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_CLUB));
  }

  @FXML
  void deleteExternalClub() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_CLUB));
  }

  // External Relation controls
  @FXML
  void newExternalRelation() {
    EventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.NEW_EXTERNAL_RELATION));
  }

  @FXML
  void amendExternalRelation() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_EXTERNAL_RELATION));
  }

  @FXML
  void deleteExternalRelation() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_EXTERNAL_RELATION));
  }

  // Internal Relation controls
  @FXML
  void newInternalRelation() {
    EventPublisher
        .publishEvent(new MenuChoiceEvent(this, MenuChoice.NEW_INTERNAL_RELATION));
  }

  @FXML
  void amendInternalRelation() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.AMEND_INTERNAL_RELATION));
  }

  @FXML
  void deleteInternalRelation() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DELETE_INTERNAL_RELATION));
  }

  // Magazine controls
  @FXML
  void magazineAddressList() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MAGAZINE_ADDRESS_LIST));
  }

  @FXML
  void cardAddressList() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.CARD_ADDRESS_LIST));
  }

  @FXML
  void memberListByNumber() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MEMBER_LIST_BY_NUMBER));
  }

  @FXML
  void memberListByName() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MEMBER_LIST_BY_NAME));
  }

  @FXML
  void magazineInvalidAddress() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MAGAZINE_INVALID_ADDRESS));
  }

  // Correspondence controls
  @FXML
  void addDocument() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.ADD_DOCUMENT));

  }

  @FXML
  void viewDocument() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.VIEW_DOCUMENT));

  }

  // Report controls
  @FXML
  void excelAll() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ALL_DATA));
  }

  @FXML
  void excelNew() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_NEW_MEMBERS));
  }

  @FXML
  void excelCancelation() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_CANCELLED_MEMBERS));
  }

  @FXML
  void excelOverdue() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_OVERDUE_MEMBERS));
  }

  @FXML
  void excelArchive() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.REPORT_ARCHIVE));
  }

  // Yearly controls
  @FXML
  void resetPayments() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RESET_PAYMENTS));
  }

  @FXML
  void directDebits() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_DIRECT_DEBITS_FILE));
  }

  @FXML
  void reconcilePayments() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.RECONCILE_PAYMENTS));
  }

  @FXML
  void reminderReport() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_REPORT));
  }

  @FXML
  void makeReminderLettersDirectDebit() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_DD));
  }

  @FXML
  void makeReminderLettersBankTranfer() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.PRODUCE_REMINDER_LETTERS_BT));
  }

  @FXML
  void yearly() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.ANNUAL_ROLLOVER));
  }

  // Maintenance controls
  @FXML
  void manageSepaAuthorizationForm() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.UPLOAD_SEPA_FORM));
  }

  @FXML
  void manageDocumentTemplates() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.TEMPLATES_OVERVIEW));
  }

  @FXML
  void backupDatabase() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_BACKUP_DATABASE));
  }

  @FXML
  void restoreDatabase() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_RESTORE_DATABASE));
  }

  @FXML
  void importFromExcel() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.IMPORT_FROM_EXCEL));
  }

  @FXML
  void settings() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.SETTINGS));
  }

  @FXML
  void manageArchive() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.MANAGE_ARCHIVE));
  }

  @FXML
  void dbconfig() {
    EventPublisher.publishEvent(new MenuChoiceEvent(this, MenuChoice.DB_CONFIG));
  }

}
