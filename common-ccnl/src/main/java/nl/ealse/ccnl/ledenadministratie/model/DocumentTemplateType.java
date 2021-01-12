package nl.ealse.ccnl.ledenadministratie.model;

import lombok.Getter;

public enum DocumentTemplateType {
  WELCOME_LETTER("Welkomsbrief"), PAYMENT_REMINDER(
      "Betaalherinnering"), MEMBERSHIP_CANCELATION_MAIL("Opzegmail");

  @Getter
  private final String description;

  private DocumentTemplateType(String description) {
    this.description = description;
  }

}
