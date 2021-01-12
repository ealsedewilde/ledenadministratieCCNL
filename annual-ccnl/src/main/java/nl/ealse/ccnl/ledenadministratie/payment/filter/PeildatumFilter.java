package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.PaymentException;

public class PeildatumFilter implements Filter {

  private final LocalDate peildatum;


  public PeildatumFilter(LocalDate peildatum) {
    try {
      this.peildatum = peildatum;
    } catch (DateTimeParseException e) {
      throw new PaymentException(e);
    }
  }


  @Override
  public boolean doFilter(IngBooking booking) {
    LocalDate boekdatum = booking.getBoekdatum();
    return !boekdatum.isBefore(peildatum);
  }

}
