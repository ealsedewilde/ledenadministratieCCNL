package nl.ealse.ccnl.ledenadministratie.payment;

public enum BookingType {

  ICDT, RCDT, RRCT, RDDT("Incasso"), CCRD("Pinbetaling"), IDDT("Stornering");

  private String omschrijving;

  BookingType() {
    this.omschrijving = "Overboeking";
  }

  BookingType(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

}
