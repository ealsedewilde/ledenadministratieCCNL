package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

@Slf4j
public class OmschrijvingStrategie implements LidnummerStrategie {

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    String omschrijving = booking.getOmschrijving().toLowerCase().trim();
    int ix = omschrijving.toLowerCase().indexOf("lid");
    if (ix > -1) {
      log.debug("lid indicatie gevonden " + omschrijving);
      NummerHelper nh = new NummerHelper(booking, omschrijving);
      nh.bepaalNummer(ix);
      if (booking.getLidnummer() == 0) {
        nh.bepaalNummer();
      }
    }
  }

  private static class NummerHelper {
    private static final String JAAR = new SimpleDateFormat("yyyy").format(new Date());

    private final IngBooking booking;
    private final char[] tekens;

    NummerHelper(IngBooking booking, String omschrijving) {
      this.booking = booking;
      tekens = omschrijving.toCharArray();
    }

    private void bepaalNummer(int ix) {
      StringBuilder sb = new StringBuilder();
      while (ix < tekens.length && !Character.isDigit(tekens[ix])) {
        ix++;
      }
      while (ix < tekens.length && Character.isDigit(tekens[ix])) {
        sb.append(tekens[ix++]);
      }
      if (sb.length() > 0 && !JAAR.equals(sb.toString())) {
        int nummer = Integer.parseInt(sb.toString());
        booking.setLidnummer(nummer);
      }
    }

    private void bepaalNummer() {
      StringBuilder sb = new StringBuilder();
      int ix = tekens.length - 1;
      while (!Character.isDigit(tekens[ix])) {
        ix--;
      }
      while (Character.isDigit(tekens[ix])) {
        sb.append(tekens[ix--]);
      }
      sb.reverse();
      if (sb.length() > 0 && !JAAR.equals(sb.toString())) {
        int nummer = Integer.parseInt(sb.toString());
        booking.setLidnummer(nummer);
      }
    }



  }

}
