package nl.ealse.ccnl.control.menu;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.support.EventPublisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

class MenuControllerTest {

  private MockedStatic<EventPublisher> context;

  private static MenuController sut;

  private ArgumentCaptor<MenuChoiceEvent> menu =
      ArgumentCaptor.forClass(MenuChoiceEvent.class);

  @BeforeEach
  void init() {
    context = mockStatic(EventPublisher.class);
  }
  
  @AfterEach
  void clear() {
    context.close();
  }
  
  @Test
  void newMember() {
    sut.newMember();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.NEW_MEMBER, event.getMenuChoice());
  }

  @Test
  void amendMember() {
    sut.amendMember();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_MEMBER, event.getMenuChoice());
  }

  @Test
  void cancelMembership() {
    sut.cancelMembership();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.CANCEL_MEMBERSHIP, event.getMenuChoice());
  }

  @Test
  void paymentAuthorization() {
    sut.paymentAuthorization();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.PAYMENT_AUTHORIZATION, event.getMenuChoice());
  }

  @Test
  void newPartner() {
    sut.newPartner();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.NEW_PARTNER, event.getMenuChoice());
  }

  @Test
  void amendPartner() {
    sut.amendPartner();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_PARTNER, event.getMenuChoice());
  }

  @Test
  void deletePartner() {
    sut.deletePartner();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_PARTNER, event.getMenuChoice());
  }

  @Test
  void newExternalClub() {
    sut.newExternalClub();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.NEW_EXTERNAL_CLUB, event.getMenuChoice());
  }

  @Test
  void amendExternalClub() {
    sut.amendExternalClub();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_EXTERNAL_CLUB, event.getMenuChoice());
  }

  @Test
  void deleteExternalClub() {
    sut.deleteExternalClub();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_EXTERNAL_CLUB, event.getMenuChoice());
  }

  @Test
  void newExternalRelation() {
    sut.newExternalRelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.NEW_EXTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void amendExternalRelation() {
    sut.amendExternalRelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_EXTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void deleteExternalRelation() {
    sut.deleteExternalRelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_EXTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void newInternalRelation() {
    sut.newInternalRelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.NEW_INTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void amendInternalRelation() {
    sut.amendInternalRelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_INTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void deleteInternalRelation() {
    sut.deleteInternalRelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_INTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void magazineAddressList() {
    sut.magazineAddressList();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.MAGAZINE_ADDRESS_LIST, event.getMenuChoice());
  }

  @Test
  void cardAddressList() {
    sut.cardAddressList();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.CARD_ADDRESS_LIST, event.getMenuChoice());
  }

  @Test
  void magazineInvalidAddress() {
    sut.magazineInvalidAddress();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.MAGAZINE_INVALID_ADDRESS, event.getMenuChoice());
  }

  @Test
  void addDocument() {
    sut.addDocument();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.ADD_DOCUMENT, event.getMenuChoice());
  }

  @Test
  void excelAll() {
    sut.excelAll();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_ALL_DATA, event.getMenuChoice());
  }

  @Test
  void excelNew() {
    sut.excelNew();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_NEW_MEMBERS, event.getMenuChoice());
  }

  @Test
  void excelCancelation() {
    sut.excelCancelation();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_CANCELLED_MEMBERS, event.getMenuChoice());
  }

  @Test
  void excelOverdue() {
    sut.excelOverdue();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_OVERDUE_MEMBERS, event.getMenuChoice());
  }

  @Test
  void excelAfterApril() {
    sut.excelAfterApril();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_AFTER_APRIL, event.getMenuChoice());
  }

  @Test
  void excelArchive() {
    sut.excelArchive();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_ARCHIVE, event.getMenuChoice());
  }

  @Test
  void viewDocument() {
    sut.viewDocument();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.VIEW_DOCUMENT, event.getMenuChoice());
  }

  @Test
  void resetPayments() {
    sut.resetPayments();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.RESET_PAYMENTS, event.getMenuChoice());
  }

  @Test
  void directDebits() {
    sut.directDebits();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_DIRECT_DEBITS_FILE, event.getMenuChoice());
  }

  @Test
  void reconcilePayments() {
    sut.reconcilePayments();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.RECONCILE_PAYMENTS, event.getMenuChoice());
  }

  @Test
  void reminderReport() {
    sut.reminderReport();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_REMINDER_REPORT, event.getMenuChoice());
  }

  @Test
  void makeReminderLettersDD() {
    sut.makeReminderLettersDirectDebit();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_REMINDER_LETTERS_DD, event.getMenuChoice());
  }

  @Test
  void makeReminderLettersBT() {
    sut.makeReminderLettersBankTranfer();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_REMINDER_LETTERS_BT, event.getMenuChoice());
  }

  @Test
  void yearly() {
    sut.yearly();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.ANNUAL_ROLLOVER, event.getMenuChoice());
  }

  @Test
  void manageSepaAuthorizationForm() {
    sut.manageSepaAuthorizationForm();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.UPLOAD_SEPA_FORM, event.getMenuChoice());
  }

  @Test
  void manageDocumentTemplates() {
    sut.manageDocumentTemplates();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.TEMPLATES_OVERVIEW, event.getMenuChoice());
  }

  @Test
  void backupDatabase() {
    sut.backupDatabase();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_BACKUP_DATABASE, event.getMenuChoice());
  }

  @Test
  void restoreDatabase() {
    sut.restoreDatabase();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_RESTORE_DATABASE, event.getMenuChoice());
  }

  @Test
  void importFromExcel() {
    sut.importFromExcel();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.IMPORT_FROM_EXCEL, event.getMenuChoice());
  }

  @Test
  void settings() {
    sut.settings();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.SETTINGS, event.getMenuChoice());
  }

  @Test
  void manageArchive() {
    sut.manageArchive();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_ARCHIVE, event.getMenuChoice());
  }

  @Test
  void DbConfig() {
    sut.dbconfig();
    context.verify(() -> EventPublisher.publishEvent(menu.capture()), atLeastOnce());
    MenuChoiceEvent event = menu.getValue();
    Assertions.assertEquals(MenuChoice.DB_CONFIG, event.getMenuChoice());
  }

  @BeforeAll
  static void setup() {
     sut = MenuController.getInstance();
  }

}
