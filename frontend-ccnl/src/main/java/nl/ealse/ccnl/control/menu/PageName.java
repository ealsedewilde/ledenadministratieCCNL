package nl.ealse.ccnl.control.menu;

import javafx.scene.Parent;
import lombok.Getter;
import nl.ealse.javafx.FXMLLoaderUtil;

/**
 * Pages to load via the PageController.
 */
public enum PageName implements PageReference {

  MEMBER_CANCEL("member/memberCancel"), MEMBER_CANCEL_MAIL(
      "member/memberCancelMail"), WELCOME_LETTER("member/welcomeLetter"),

  EXTERNAL_CLUB_DELETE("club/externalClubDelete"),

  EXTERNAL_RELATION_DELETE("external/externalRelationDelete"),

  INTERNAL_RELATION_DELETE("internal/internalRelationDelete"),

  PARTNER_DELETE("partner/partnerDelete"),

  ADD_DOCUMENT("document/addDocument"), VIEW_DOCUMENTS("document/viewDocuments"),

  MAGAZINE_ADDRESS_LIST("magazine/magazineAddressList"), MAGAZINE_INVALID_ADDRESS(
      "magazine/magazineInvalidAddress"),

  DIRECT_DEBITS("annual/directDebits"), RECONCILE_PAYMENTS(
      "annual/reconcilePayments"), PAYMENT_REMINDER_LETTERS(
          "annual/paymentReminderLetters"), ANNUAL_ROLLOVER("annual/annualRollover"),

  EXCEL_IMPORT("settings/excelImport"), TEMPLATES_OVERVIEW(
      "settings/templatesOverview"), MANAGE_TEMPLATE("settings/manageTemplate"), MANAGE_DATABASE(
          "manageDatabase"), SETTINGS(
              "settings/settings"), MANAGE_ARCHIVE("settings/manageArchive");

  @Getter
  private final String fxmlName;

  private Parent page;

  PageName(String fxmlName) {
    this.fxmlName = fxmlName;
  }

  /**
   * Get the loaded fxml page.
   *
   * @return the loaded fxml page
   */
  @Override
  public Parent getPage() {
    if (page == null) {
      page = FXMLLoaderUtil.getPage(fxmlName);
    }
    return page;
  }

}
