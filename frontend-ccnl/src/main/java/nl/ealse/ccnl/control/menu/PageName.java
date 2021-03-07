package nl.ealse.ccnl.control.menu;

import lombok.Getter;
import nl.ealse.javafx.PageId;

public enum PageName {

  MEMBER_PERSONAL("member/memberPersonal"), MEMBER_ADDRESS(
      "member/memberAddress"), MEMBER_FINANCIAL("member/memberFinancial"), MEMBER_EXTRA(
          "member/memberExtraInfo"), MEMBER_CANCEL(
              "member/memberCancel"), MEMBER_CANCEL_MAIL("member/memberCancelMail"),

  MEMBER_SEARCH("member/memberSearch"),

  WELCOME_TEXT_HELP("dialog/texthelp"), REMINDER_TEXT_HELP("dialog/texthelp"), MAIL_HELP(
      "dialog/mailhelp"), MANAGE_TEMPLATE_TEXT_HELP(
          "dialog/texthelp"), MANAGE_MAIL_HELP("dialog/mailhelp"),

  SEPA_AUTHORIZATION_ADD("document/sepaAuthorizationAdd"), SEPA_AUTHORIZATION_SHOW(
      "document/sepaAuthorizationShow"), WELCOME_LETTER(
          "member/welcomeLetter"), WELCOME_LETTER_SHOW(
              "document/welcomeLetterShow"), ADD_IBAN_NUMBER("dialog/addIban"),

  EXTERNAL_CLUB_PERSONAL("club/externalClubPersonal"), EXTERNAL_CLUB_ADDRESS(
      "club/externalClubAddress"), EXTERNAL_CLUB_SEARCH(
          "club/externalClubSearch"), EXTERNAL_CLUB_DELETE("club/externalClubDelete"),

  EXTERNAL_RELATION_PERSONAL("external/externalRelationPersonal"), EXTERNAL_RELATION_ADDRESS(
      "external/externalRelationAddress"), EXTERNAL_RELATION_SEARCH(
          "external/externalRelationSearch"), EXTERNAL_RELATION_DELETE(
              "external/externalRelationDelete"),

  INTERNAL_RELATION_PERSONAL("internal/internalRelationPersonal"), INTERNAL_RELATION_ADDRESS(
      "internal/internalRelationAddress"), INTERNAL_RELATION_SEARCH(
          "internal/internalRelationSearch"), INTERNAL_RELATION_DELETE(
              "internal/internalRelationDelete"),

  PARTNER_PERSONAL("partner/partnerPersonal"), PARTNER_ADDRESS(
      "partner/partnerAddress"), PARTNER_SEARCH(
          "partner/partnerSearch"), PARTNER_DELETE("partner/partnerDelete"),

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
              "settings/settingsEdit"), MANAGE_ARCHIVE("settings/manageArchive"),

  LOGO("logo");

  @Getter
  private final PageId id;

  PageName(String fxmlName) {
    this.id = new PageId(this.name(), fxmlName);
  }

}
