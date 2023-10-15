package nl.ealse.ccnl.control.menu;

import static nl.ealse.ccnl.control.menu.ChoiceGroup.COMMANDS;
import static nl.ealse.ccnl.control.menu.ChoiceGroup.REPORTS;
import static nl.ealse.ccnl.control.menu.ChoiceGroup.SEARCH_CLUB;
import static nl.ealse.ccnl.control.menu.ChoiceGroup.SEARCH_EXTERNAL;
import static nl.ealse.ccnl.control.menu.ChoiceGroup.SEARCH_INTERNAL;
import static nl.ealse.ccnl.control.menu.ChoiceGroup.SEARCH_MEMBER;
import static nl.ealse.ccnl.control.menu.ChoiceGroup.SEARCH_PARTNER;
import lombok.Getter;

/**
 * A MenuChoice for every choice made in the application's menu.
 * 
 * @author ealse
 *
 */
public enum MenuChoice {
  NEW_MEMBER, AMEND_MEMBER(SEARCH_MEMBER), CANCEL_MEMBERSHIP(SEARCH_MEMBER), PAYMENT_AUTHORIZATION(
      SEARCH_MEMBER), ADD_IBAN_NUMBER,
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
  MAGAZINE_ADDRESS_LIST, CARD_ADDRESS_LIST(true), MEMBER_LIST_BY_NUMBER(true), MEMBER_LIST_BY_NAME(true), MAGAZINE_INVALID_ADDRESS(SEARCH_MEMBER),
  //
  ADD_DOCUMENT(SEARCH_MEMBER), VIEW_DOCUMENT(SEARCH_MEMBER), DELETE_DOCUMENT(SEARCH_MEMBER),
  //
  REPORT_ALL_DATA(REPORTS, true), REPORT_NEW_MEMBERS(REPORTS, true), REPORT_OVERDUE_MEMBERS(
      REPORTS, true), REPORT_CANCELLED_MEMBERS(REPORTS, true), REPORT_ARCHIVE(REPORTS, true),
  //
  RESET_PAYMENTS(COMMANDS, true), ANNUAL_ROLLOVER, PRODUCE_DIRECT_DEBITS_FILE,
  //
  PRODUCE_REMINDER_REPORT(COMMANDS, true), PRODUCE_REMINDER_LETTERS_DD, PRODUCE_REMINDER_LETTERS_BT,
  //
  RECONCILE_PAYMENTS, HANDLE_OVERDUE,
  //
  UPLOAD_SEPA_FORM(COMMANDS, true), TEMPLATES_OVERVIEW,

  MANAGE_BACKUP_DATABASE(COMMANDS, true), MANAGE_RESTORE_DATABASE(true),

  IMPORT_FROM_EXCEL,

  SETTINGS, MANAGE_ARCHIVE;

  @Getter
  private ChoiceGroup group;

  @Getter
  private boolean command = false;


  private MenuChoice() {

  }

  private MenuChoice(ChoiceGroup group) {
    this.group = group;
  }

  private MenuChoice(boolean command) {
    this.command = command;
  }

  private MenuChoice(ChoiceGroup group, boolean command) {
    this.group = group;
    this.command = command;
  }

}
