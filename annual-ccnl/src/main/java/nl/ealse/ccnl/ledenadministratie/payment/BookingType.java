package nl.ealse.ccnl.ledenadministratie.payment;

public enum BookingType {
  
  // Issued, Received or Cash

  ICDT, RCDT, IRCT, RRCT, RDDT("Ontvangen Incasso"), CCRD("Pinbetaling"), IDDT("Incacco/Stornering"), OTHER("n.v.t.");
  
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
    return OTHER;
    
  }

}
