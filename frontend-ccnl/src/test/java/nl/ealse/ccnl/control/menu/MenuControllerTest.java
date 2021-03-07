package nl.ealse.ccnl.control.menu;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.ealse.ccnl.event.ExternalClubSelectionEvent;
import nl.ealse.ccnl.event.ExternalOtherSelectionEvent;
import nl.ealse.ccnl.event.InternalRelationSelectionEvent;
import nl.ealse.ccnl.event.MemberSeLectionEvent;
import nl.ealse.ccnl.event.MenuChoiceEvent;
import nl.ealse.ccnl.event.PartnerSelectionEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationContext;

class MenuControllerTest {

  private static ApplicationContext springContext;
  private static PageController pageController;

  private static MenuController sut;

  private ArgumentCaptor<MemberSeLectionEvent> ams =
      ArgumentCaptor.forClass(MemberSeLectionEvent.class);
  private ArgumentCaptor<PartnerSelectionEvent> aps =
      ArgumentCaptor.forClass(PartnerSelectionEvent.class);
  private ArgumentCaptor<ExternalClubSelectionEvent> acs =
      ArgumentCaptor.forClass(ExternalClubSelectionEvent.class);
  private ArgumentCaptor<ExternalOtherSelectionEvent> aos =
      ArgumentCaptor.forClass(ExternalOtherSelectionEvent.class);
  private ArgumentCaptor<InternalRelationSelectionEvent> ais =
      ArgumentCaptor.forClass(InternalRelationSelectionEvent.class);
  private ArgumentCaptor<MenuChoiceEvent> am = ArgumentCaptor.forClass(MenuChoiceEvent.class);

  @Test
  void newMember() {
    sut.newMember();
    verify(springContext, atLeastOnce()).publishEvent(ams.capture());
    MemberSeLectionEvent event = ams.getValue();
    Assertions.assertEquals(MenuChoice.NEW_MEMBER, event.getMenuChoice());
  }

  @Test
  void amendMember() {
    sut.amendMember();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_MEMBER, event.getMenuChoice());
  }

  @Test
  void cancelMembership() {
    sut.cancelMembership();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.CANCEL_MEMBERSHIP, event.getMenuChoice());
  }

  @Test
  void paymentAuthorization() {
    sut.paymentAuthorization();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.PAYMENT_AUTHORIZATION, event.getMenuChoice());
  }

  @Test
  void newPartner() {
    sut.newPartner();
    verify(springContext, atLeastOnce()).publishEvent(aps.capture());
    PartnerSelectionEvent event = aps.getValue();
    Assertions.assertEquals(MenuChoice.NEW_PARTNER, event.getMenuChoice());
  }

  @Test
  void amendPartner() {
    sut.amendPartner();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_PARTNER, event.getMenuChoice());
  }

  @Test
  void deletePartner() {
    sut.deletePartner();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_PARTNER, event.getMenuChoice());
  }

  @Test
  void newExternalClub() {
    sut.newExternalClub();
    verify(springContext, atLeastOnce()).publishEvent(acs.capture());
    ExternalClubSelectionEvent event = acs.getValue();
    Assertions.assertEquals(MenuChoice.NEW_EXTERNAL_CLUB, event.getMenuChoice());
  }

  @Test
  void amendExternalClub() {
    sut.amendExternalClub();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_EXTERNAL_CLUB, event.getMenuChoice());
  }

  @Test
  void deleteExternalClub() {
    sut.deleteExternalClub();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_EXTERNAL_CLUB, event.getMenuChoice());
  }

  @Test
  void newExternalRelation() {
    sut.newExternalRelation();
    verify(springContext, atLeastOnce()).publishEvent(aos.capture());
    ExternalOtherSelectionEvent event = aos.getValue();
    Assertions.assertEquals(MenuChoice.NEW_EXTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void amendExternalRelation() {
    sut.amendExternalRelation();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_EXTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void deleteExternalRelation() {
    sut.deleteExternalRelation();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_EXTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void newInternalRelation() {
    sut.newInternalRelation();
    verify(springContext, atLeastOnce()).publishEvent(ais.capture());
    InternalRelationSelectionEvent event = ais.getValue();
    Assertions.assertEquals(MenuChoice.NEW_INTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void amendInternalRelation() {
    sut.amendInternalRelation();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.AMEND_INTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void deleteInternalRelation() {
    sut.deleteInternalRelation();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.DELETE_INTERNAL_RELATION, event.getMenuChoice());
  }

  @Test
  void magazineAddressList() {
    sut.magazineAddressList();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.MAGAZINE_ADDRESS_LIST, event.getMenuChoice());
  }

  @Test
  void cardAddressList() {
    sut.cardAddressList();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.CARD_ADDRESS_LIST, event.getMenuChoice());
  }

  @Test
  void magazineInvalidAddress() {
    sut.magazineInvalidAddress();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.MAGAZINE_INVALID_ADDRESS, event.getMenuChoice());
  }

  @Test
  void addDocument() {
    sut.addDocument();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.ADD_DOCUMENT, event.getMenuChoice());
  }

  @Test
  void excelAll() {
    sut.excelAll();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_ALL_DATA, event.getMenuChoice());
  }

  @Test
  void excelNew() {
    sut.excelNew();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_NEW_MEMBERS, event.getMenuChoice());
  }

  @Test
  void excelCancelation() {
    sut.excelCancelation();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_CANCELLED_MEMBERS, event.getMenuChoice());
  }

  @Test
  void excelOverdue() {
    sut.excelOverdue();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_OVERDUE_MEMBERS, event.getMenuChoice());
  }

  @Test
  void excelArchive() {
    sut.excelArchive();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.REPORT_ARCHIVE, event.getMenuChoice());
  }

  @Test
  void viewDocument() {
    sut.viewDocument();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.VIEW_DOCUMENT, event.getMenuChoice());
  }

  @Test
  void resetPayments() {
    sut.resetPayments();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.RESET_PAYMENTS, event.getMenuChoice());
  }

  @Test
  void directDebits() {
    sut.directDebits();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_DIRECT_DEBITS_FILE, event.getMenuChoice());
  }

  @Test
  void reconcilePayments() {
    sut.reconcilePayments();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.RECONCILE_PAYMENTS, event.getMenuChoice());
  }

  @Test
  void makeReminderLettersDD() {
    sut.makeReminderLettersDD();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_REMINDER_LETTERS_DD, event.getMenuChoice());
  }

  @Test
  void makeReminderLettersBT() {
    sut.makeReminderLettersBT();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.PRODUCE_REMINDER_LETTERS_BT, event.getMenuChoice());
  }

  @Test
  void yearly() {
    sut.yearly();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.ANNUAL_ROLLOVER, event.getMenuChoice());
  }

  @Test
  void manageSepaAuthorizationForm() {
    sut.manageSepaAuthorizationForm();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_SEPA_FORM, event.getMenuChoice());
  }

  @Test
  void manageDocumentTemplates() {
    sut.manageDocumentTemplates();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.TEMPLATES_OVERVIEW, event.getMenuChoice());
  }

  @Test
  void backupDatabase() {
    sut.backupDatabase();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_BACKUP_DATABASE, event.getMenuChoice());
  }

  @Test
  void restoreDatabase() {
    sut.restoreDatabase();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_RESTORE_DATABASE, event.getMenuChoice());
  }

  @Test
  void importFromExcel() {
    sut.importFromExcel();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.IMPORT_FROM_EXCEL, event.getMenuChoice());
  }

  @Test
  void settings() {
    sut.settings();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.SETTINGS, event.getMenuChoice());
  }

  @Test
  void manageArchive() {
    sut.manageArchive();
    verify(springContext, atLeastOnce()).publishEvent(am.capture());
    MenuChoiceEvent event = am.getValue();
    Assertions.assertEquals(MenuChoice.MANAGE_ARCHIVE, event.getMenuChoice());
  }

  @BeforeAll
  static void setup() {
    springContext = mock(ApplicationContext.class);
    pageController = mock(PageController.class);
    sut = new MenuController(springContext, pageController);
  }

}
