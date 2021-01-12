package nl.ealse.ccnl.ledenadministratie.payment.strategy;

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
public class NaamStrategie implements LidnummerStrategie {
  private final List<Integer> nummers;
  private final List<Member> members;
  private Map<Integer, Integer> scores = new HashMap<>();

  public NaamStrategie(List<Integer> nummers, List<Member> members) {
    this.nummers = nummers;
    this.members = members;
  }

  @Override
  public void bepaalLidnummer(IngBooking booking) {
    if (booking.getLidnummer() > 0) {
      return;
    }
    String naam = bepaalNaam(booking);
    scores.clear();
    nummers.clear();

    // match op het ledenbestand
    for (Member member : members) {
      String nm = member.getLastName().toUpperCase();
      if (naam.endsWith(nm)) {
        scores.put(member.getMemberNumber(), bepaalScore(naam, nm));
      }
    }

    // Op dit punt hebben we alle mogelijke matchs bepaald.
    // Nu omzetten naar gewogen matches.
    handleScore();

    // We hebben nu gewogen matches (dwz alleen de overeenkomsten met het zwaarste gewicht)
    if (nummers.size() == 1) {
      booking.setLidnummer(nummers.get(0));
      log.debug(String.format("lid %s bij naam %s", nummers.get(0), naam));
    } else {
      log.debug(String.format("%s resultaten bij naam %s", nummers.size(), booking.getNaam()));
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
          nummers.add(entry.getKey());
        }
      }
    } else if (scores.size() == 1) {
      nummers.add(scores.keySet().iterator().next());
    }
  }

  private String bepaalNaam(IngBooking booking) {
    String naam = booking.getNaam().toUpperCase();
    if (naam.endsWith(" EO")) {
      naam = naam.substring(0, naam.length() - 3);
    }
    // Als de VOORLETTERS achter de naam staan dan deze weghalen
    while (Character.isSpaceChar(naam.toCharArray()[naam.length() - 2])) {
      naam = naam.substring(0, naam.length() - 2);
    }
    return naam;
  }

  /**
   * Op hoeveel letters is er een overeenkomst? Omdatde naam ook voorvoegsel kan bevatten, wordt er
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
