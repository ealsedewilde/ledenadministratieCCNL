package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

/**
 * Lidnummer bepalen op basis van de naam.
 * 
 * @author Ealse
 */
@Slf4j
public class NaamStrategie extends BetalingStrategie {

  private final List<Member> members;
  private Map<Integer, Integer> scores = new HashMap<>();

  public NaamStrategie(List<Member> members) {
    super(new ArrayList<>());
    this.members = members;
  }

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    getNummers().clear();
    String naam = bepaalNaam(booking);
    scores.clear();

    // match op het ledenbestand
    for (Member member : members) {
      String nm = member.getLastName().toUpperCase();
      if (naam.endsWith(nm)) {
        scores.put(member.getMemberNumber(), bepaalScore(naam, nm));
      }
    }

    // Op dit punt hebben we alle mogelijke matches bepaald.
    // Nu omzetten naar gewogen matches.
    handleScore();

    if (getNummers().size() > 1) {
      // Er zijn meerdere leden met dezelfde naam gevonden
      String omschrijving = booking.getOmschrijving();
      for (Integer nr : getNummers()) {
        if (omschrijving.indexOf(nr.toString()) > -1) {
          booking.setLidnummer(nr);
          log.debug(String.format("lid %s bij naam %s", nr, booking.getNaam()));
          break;
        }
      }
      if (booking.getLidnummer() == 0) {
        logResult(booking);
      }
    } else if (!getNummers().isEmpty()) { 
      Integer nr = getNummers().get(0);
      booking.setLidnummer(nr);
      log.debug(String.format("lid %s bij naam %s", nr, booking.getNaam()));
    } else {
      logResult(booking);
    }
  }

  private void handleScore() {
    if (scores.size() > 1) {
      int score = 0;
      for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
        if (entry.getValue() > score) {
          score = entry.getValue();
        }
      }

      for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
        if (entry.getValue() == score) {
          getNummers().add(entry.getKey());
        }
      }
    } else if (scores.size() == 1) {
      getNummers().add(scores.keySet().iterator().next());
    }
  }

  private String bepaalNaam(IngBooking booking) {
    String naam = booking.getNaam().toUpperCase();
    if (naam.endsWith(" EO")) {
      naam = naam.substring(0, naam.length() - 3);
    }
    // Als de VOORLETTERS achter de naam staan dan deze weghalen
    String[] parts = naam.split(" ");
    boolean initial = false; 
    for (int q = parts.length -1 ; q >= 0 ; q--) {
      String p = parts[q];
      if (p.length() == 1) {
        initial = true;
      } else if (initial) {
        int ix = naam.indexOf(p) + p.length();
        naam =  naam.substring(0, ix);
        break;
      }
    }
    return naam;
  }

  /**
   * Op hoeveel letters is er een overeenkomst? 
   * Omdat e naam ook voorvoegsel kan bevatten, wordt er
   * van achter naar voren gewerkt.
   * 
   * @param naam
   * @param lidnaam
   * @return
   */
  private int bepaalScore(String naam, String lidnaam) {
    int ni = naam.length();
    int li = lidnaam.length();
    char[] n = naam.toCharArray();
    char[] l = lidnaam.toCharArray();
    int score = 0;
    while (ni > 0 && li > 0) {
      if (n[--ni] == l[--li]) {
        score++;
      } else {
        break;
      }
    }
    return score;
  }

}
