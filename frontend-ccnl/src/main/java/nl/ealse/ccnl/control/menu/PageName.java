package nl.ealse.ccnl.control.menu;

import lombok.Getter;

/**
 * Pages to load via the PageController.
 */
public enum PageName {

  MEMBER_SEARCH("member/memberSearch"), MEMBER_CANCEL("member/memberCancel"), MEMBER_CANCEL_MAIL(
      "member/memberCancelMail"), WELCOME_LETTER("member/welcomeLetter"),

  EXTERNAL_CLUB_SEARCH("club/externalClubSearch"), EXTERNAL_CLUB_DELETE("club/externalClubDelete"),

  EXTERNAL_RELATION_SEARCH("external/externalRelationSearch"), EXTERNAL_RELATION_DELETE(
      "external/externalRelationDelete"),

  INTERNAL_RELATION_SEARCH("internal/internalRelationSearch"), INTERNAL_RELATION_DELETE(
      "internal/internalRelationDelete"),

  PARTNER_SEARCH("partner/partnerSearch"), PARTNER_DELETE("partner/partnerDelete"),

  ADD_DOCUMENT("document/addDocument"), VIEW_DOCUMENTS("document/viewDocuments"),

  MAGAZINE_ADDRESS_LIST("magazine/magazineAddressList"), MAGAZINE_INVALID_ADDRESS(
      "magazine/magazineInvalidAddress"),

  DIRECT_DEBITS("annual/directDebits"), RECONCILE_PAYMENTS(
      "annual/reconcilePayments"), PAYMENT_REMINDER_LETTERS(
          "annual/paymentReminderLetters"), ANNUAL_ROLLOVER("annual/annualRollover"),

  EXCEL_IMPORT("settings/excelImport"), TEMPLATES_OVERVIEW(
      "settings/templatesOverview"), MANAGE_TEMPLATE("settings/manageTemplate"), MANAGE_DATABASE(
          "manageDatabase"), SETTINGS("settings/settings"), MANAGE_ARCHIVE("settings/manageArchive");

  @Getter
  private final PageId id;

  PageName(String fxmlName) {
    this.id = new PageId(this.name(), fxmlName);
  }

}
