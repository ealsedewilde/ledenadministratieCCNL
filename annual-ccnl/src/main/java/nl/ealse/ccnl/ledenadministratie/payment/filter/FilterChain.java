package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public final class FilterChain {

  private static final List<Filter> FILTERS = new ArrayList<>();

  public FilterChain(List<Member> members, LocalDate referenceDate) {
    FILTERS.add(new PeildatumFilter(referenceDate));
    FILTERS.add(new BoekingTypeFilter());
    FILTERS.add(new BedragFilter());
    FILTERS.add(new OmschrijvingFilter());
    FILTERS.add(new LidnummerFilter(members));
  }

  public boolean filter(IngBooking booking) {
    for (Filter filter : FILTERS) {
      if (!filter.doFilter(booking)) {
        return false;
      }
    }
    return true;
  }

}
