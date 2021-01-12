package nl.ealse.ccnl.ledenadministratie.model;

import lombok.Getter;

public enum DocumentType {
  /**
   * Letter to a new member.
   */
  WELCOME_LETTER("Welkomsbrief nieuw lid"),
  /**
   * Letter from the annual process regarding overdue payment.
   */
  PAYMENT_REMINDER("Herinneringsbrief contributie"),
  /**
   * Mail for members that don't want to renew their membership
   */
  MEMBERSHIP_CANCELATION_MAIL("Bevestigingsmail opzegging"),
  /**
   * Filled SEPA_authorization document with authorization by the Member.
   */
  SEPA_AUTHORIZATION("SEPA-machtiging"),
  /**
   * Blank SEPA_authorization document
   */
  SEPA_AUTHORIZATION_FORM("N/A"),
  /**
   * Document not managed by this application
   */
  OTHER("Overig");

  @Getter
  private final String description;

  private DocumentType(String description) {
    this.description = description;
  }

}
