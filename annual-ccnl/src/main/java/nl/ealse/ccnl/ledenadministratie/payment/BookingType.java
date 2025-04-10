package nl.ealse.ccnl.ledenadministratie.payment;

public enum BookingType {
  
   /**
   * Issued Credit Transfers
   */
  ICDT, 
  /**
   * Received Credit Transfers
   */
  RCDT,  
  /**
   * Issued Realtime CreditTransfers
   */
  IRCT("Terugbetaling"), 
  /**
   * Received Realtime Credit Transfers
   */
  RRCT,
  /**
   * Received Direct Debits
   */
  RDDT("Ontvangen Incasso"), 
  /**
   * Customer Card Transactions
   */
  CCRD("Pinbetaling"), 
  /**
   * Issued Direct Debits
   */
  IDDT("Stornering"),
  /**
   * Placeholder for other Transaction codes that are irrelevant for us.
   */
  OTHER("n.v.t.");
  
  // Used on screen in 'Betaal info'
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
