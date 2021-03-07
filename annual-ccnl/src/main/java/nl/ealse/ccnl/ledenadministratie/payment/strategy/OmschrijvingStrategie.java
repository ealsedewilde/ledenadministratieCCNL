package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

@Slf4j
public class OmschrijvingStrategie implements LidnummerStrategie {
   String jaar = new SimpleDateFormat("yyyy").format(new Date());

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    String omschrijving = booking.getOmschrijving().toLowerCase();
    int ix = omschrijving.indexOf("lid");
    if (ix > -1) {
      log.debug("lid indicatie gevonden");
      char[] tekens = omschrijving.substring(ix + 3).toCharArray();
      boolean go = true;
      StringBuilder sb = new StringBuilder();
      for (char teken : tekens) {
        if (Character.isDigit(teken)) {
          go = false;
          sb.append(teken);
        } else if (!go) {
          break;
        }
      }
      log.debug("lid indicatie gevonden " + sb.toString());
      if (sb.length() > 0 && !jaar.equals(sb.toString())) {
        int nummer = Integer.parseInt(sb.toString());
        booking.setLidnummer(nummer);
      }
    }

  }

}
