package nl.ealse.ccnl.ledenadministratie.payment;

public enum BookingType {

  ICDT, RCDT, IRCT("Terugbetaling"), RRCT, RDDT("Incasso"), CCRD("Pinbetaling"), IDDT("Stornering");

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
