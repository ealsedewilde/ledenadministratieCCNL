package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

@Slf4j
public class OmschrijvingStrategie implements LidnummerStrategie {
  private List<Integer> nummers;
  String jaar = new SimpleDateFormat("yyyy").format(new Date());

  public OmschrijvingStrategie(List<Integer> nummers) {
    this.nummers = nummers;
  }

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    if (booking.getLidnummer() > 0) {
      return;
    }
    String omschrijving = booking.getOmschrijving();
    for (Integer nr : nummers) {
      if (omschrijving.indexOf(nr.toString()) > -1) {
        booking.setLidnummer(nr);
        log.debug(String.format("lid %s bij naam %s", nr, booking.getNaam()));
        return;
      }
    }
    omschrijving = omschrijving.toLowerCase();
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
