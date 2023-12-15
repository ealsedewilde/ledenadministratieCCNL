package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public final class FilterChain {
  private final List<Filter> filters = new ArrayList<>();
  private final LidnummerFilter lidnummerFilter;

  public FilterChain(List<Member> members, LocalDate referenceDate) {
    lidnummerFilter = new LidnummerFilter(members);
    filters.add(new PeildatumFilter(referenceDate));
    filters.add(new BedragFilter());
    filters.add(new BoekingTypeFilter());
  }

  public boolean filter(IngBooking booking) {
    for (Filter filter : filters) {
      if (!filter.doFilter(booking)) {
        return false;
      }
    }
    lidnummerFilter.doFilter(booking);
    return true;
  }

}
