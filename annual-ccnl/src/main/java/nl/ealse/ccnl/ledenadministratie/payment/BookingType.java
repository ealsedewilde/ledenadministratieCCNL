package nl.ealse.ccnl.ledenadministratie.payment;

public enum BookingType {

  ICDT("Overbooking"), RCDT("Overbooking"), RRCT("Overbooking"), RDDT("Incasso"), CCRD(
      "Pinbetaling"), IDDT("Stornering");

  private String omschrijving;

  BookingType(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

}
