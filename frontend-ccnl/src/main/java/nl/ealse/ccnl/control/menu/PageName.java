package nl.ealse.ccnl.control.menu;

import lombok.Getter;
import nl.ealse.javafx.PageId;

/**
 * Pages to load via the PageController.
 */
public enum PageName {

  MEMBER_SEARCH("member/memberSearch"), MEMBER_FORM("form/form"), MEMBER_CANCEL(
      "member/memberCancel"), MEMBER_CANCEL_MAIL("member/memberCancelMail"),

  WELCOME_TEXT_HELP(), REMINDER_TEXT_HELP(), MANAGE_TEMPLATE_TEXT_HELP(), MAIL_HELP(
      "dialog/mailhelp"), MANAGE_MAIL_HELP("dialog/mailhelp"),

  SEPA_AUTHORIZATION_ADD("document/sepaAuthorizationAdd"), SEPA_AUTHORIZATION_SHOW(
      "document/sepaAuthorizationShow"), WELCOME_LETTER(
          "member/welcomeLetter"), WELCOME_LETTER_SHOW(
              "document/welcomeLetterShow"), ADD_IBAN_NUMBER("dialog/addIban"),

  EXTERNAL_CLUB_SEARCH("club/externalClubSearch"), EXTERNAL_CLUB_FORM(
      "form/form"), EXTERNAL_CLUB_DELETE("club/externalClubDelete"),

  EXTERNAL_RELATION_SEARCH("external/externalRelationSearch"), EXTERNAL_RELATION_FORM(
      "form/form"), EXTERNAL_RELATION_DELETE("external/externalRelationDelete"),

  INTERNAL_RELATION_SEARCH("internal/internalRelationSearch"), INTERNAL_RELATION_FORM(
      "form/form"), INTERNAL_RELATION_DELETE("internal/internalRelationDelete"),

  PARTNER_SEARCH("partner/partnerSearch"), PARTNER_FORM("form/form"), PARTNER_DELETE(
      "partner/partnerDelete"),

  ADD_DOCUMENT("document/addDocument"), VIEW_DOCUMENTS(
      "document/viewDocuments"), VIEW_DOCUMENT_SHOW("document/viewDocumentShow"),

  MAGAZINE_ADDRESS_LIST("magazine/magazineAddressList"), MAGAZINE_INVALID_ADDRESS(
      "magazine/magazineInvalidAddress"),

  DIRECT_DEBITS("annual/directDebits"), DIRECT_DEBITS_SETTINGS(
      "dialog/directDebitsSettings"), DIRECT_DEBITS_MESSAGES(
          "dialog/directDebitMessages"), RECONCILE_PAYMENTS(
              "annual/reconcilePayments"), RECONCILE_MESSAGES(
                  "dialog/reconciliationMessages"), PAYMENT_REMINDER_LETTERS(
                      "annual/paymentReminderLetters"), PAYMENT_REMINDER_LETTER_SHOW(
                          "annual/paymentReminderLetterShow"), ANNUAL_ROLLOVER(
                              "annual/annualRollover"),


  EXCEL_IMPORT("settings/excelImport"), TEMPLATES_OVERVIEW(
      "settings/templatesOverview"), MANAGE_TEMPLATE("settings/manageTemplate"), MANAGE_DATABASE(
          "manageDatabase"), SETTINGS("settings/settings"), SETTINGS_EDIT(
              "settings/settingsEdit"), MANAGE_ARCHIVE("settings/manageArchive");

  @Getter
  private final PageId id;

  PageName() {
    this.id = new PageId(this.name(), "dialog/texthelp");
  }

  PageName(String fxmlName) {
    this.id = new PageId(this.name(), fxmlName);
  }

}
