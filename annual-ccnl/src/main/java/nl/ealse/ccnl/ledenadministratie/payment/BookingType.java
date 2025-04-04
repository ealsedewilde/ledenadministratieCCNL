package nl.ealse.ccnl.ledenadministratie.payment;

public enum BookingType {

  ICDT, RCDT, IRCT("Terugbetaling"), RRCT, RDDT("Incasso"), CCRD("Pinbetaling"), IDDT("Stornering"), NA("n.v.t.");
  
  private final String omschrijving;
  
  BookingType() {
    this.omschrijving = "Overboeking";
  }

  BookingType(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getOmschrijving() {
    return omschrijving;
  }
  
  public static BookingType toBookingType(String code) {
    for (BookingType t : BookingType.values()) {
      if (t.name().equals(code)) {
        return t;
      }
    }
    return NA;
    
  }

}
