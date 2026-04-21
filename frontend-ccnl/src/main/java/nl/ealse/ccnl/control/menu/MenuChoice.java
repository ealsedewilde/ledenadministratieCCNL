package nl.ealse.ccnl.control.menu;

import static nl.ealse.ccnl.control.menu.Link.REPORTS;
import static nl.ealse.ccnl.control.menu.Link.SEARCH_CLUB;
import static nl.ealse.ccnl.control.menu.Link.SEARCH_EXTERNAL;
import static nl.ealse.ccnl.control.menu.Link.SEARCH_INTERNAL;
import static nl.ealse.ccnl.control.menu.Link.SEARCH_MEMBER;
import static nl.ealse.ccnl.control.menu.Link.SEARCH_PARTNER;
import static nl.ealse.ccnl.control.menu.Link.SEARCH_PA_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/**
 * A MenuChoice for every choice made in the application's menu.
 *
 * @author ealse
 *
 */
public enum MenuChoice implements Step {
  NEW_MEMBER, WELCOME_LETTER(SEARCH_MEMBER), AMEND_MEMBER(SEARCH_MEMBER), CANCEL_MEMBERSHIP(
      SEARCH_MEMBER), PAYMENT_AUTHORIZATION(SEARCH_PA_MEMBER),
  //
  NEW_PARTNER, AMEND_PARTNER(SEARCH_PARTNER), DELETE_PARTNER(SEARCH_PARTNER),
  //
  NEW_EXTERNAL_CLUB, AMEND_EXTERNAL_CLUB(SEARCH_CLUB), DELETE_EXTERNAL_CLUB(SEARCH_CLUB),
  //
  NEW_EXTERNAL_RELATION, AMEND_EXTERNAL_RELATION(SEARCH_EXTERNAL), DELETE_EXTERNAL_RELATION(
      SEARCH_EXTERNAL),
  //
  NEW_INTERNAL_RELATION, AMEND_INTERNAL_RELATION(SEARCH_INTERNAL), DELETE_INTERNAL_RELATION(
      SEARCH_INTERNAL),
  //
  MAGAZINE_ADDRESS_LIST, CARD_ADDRESS_LIST(true), MEMBER_LIST_BY_NUMBER(true), MEMBER_LIST_BY_NAME(
      true), MAGAZINE_INVALID_ADDRESS(SEARCH_MEMBER),
  //
  ADD_DOCUMENT(SEARCH_MEMBER), VIEW_DOCUMENT(SEARCH_MEMBER), DELETE_DOCUMENT(SEARCH_MEMBER),
  //
  REPORT_ALL_DATA(true, REPORTS), REPORT_NEW_MEMBERS(true, REPORTS), REPORT_OVERDUE_MEMBERS(true,
      REPORTS), REPORT_CANCELLED_MEMBERS(true,
          REPORTS), REPORT_AFTER_APRIL(true, REPORTS), REPORT_ARCHIVE(true, REPORTS),
  //
  RESET_PAYMENTS(true), ANNUAL_ROLLOVER, PRODUCE_DIRECT_DEBITS_FILE,
  //
  PRODUCE_REMINDER_REPORT(true), PRODUCE_REMINDER_PARTLY_PAID_REPORT(true), 
      PRODUCE_REMINDER_LETTERS_DD, PRODUCE_REMINDER_LETTERS_BT, PRODUCE_REMINDER_LETTERS_BT_X,
  //
  RECONCILE_PAYMENTS, HANDLE_OVERDUE,
  //
  UPLOAD_SEPA_FORM(true), TEMPLATES_OVERVIEW,
  //
  MANAGE_BACKUP_DATABASE(true), MANAGE_RESTORE_DATABASE(true),
  //
  IMPORT_FROM_EXCEL,
  //
  SETTINGS, MANAGE_ARCHIVE,
  //
  DB_CONFIG(true),
  //
  LOGO;

  @Getter
  private List<Step> steps = new ArrayList<>();

  @Getter
  private boolean command = false;

  private MenuChoice(Link... chain) {
    this.steps.addAll(Arrays.asList(chain));
    this.steps.add(this);
  }

  private MenuChoice(boolean command, Link... chain) {
    this(chain);
    this.command = command;
  }

}
