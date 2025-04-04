package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;

public final class FilterChain {
  private final List<Filter> filters = new ArrayList<>();

  public FilterChain(List<String> messageList, List<Member> members, LocalDate referenceDate) {
    filters.add(new PeildatumFilter(referenceDate));
    filters.add(new BoekingTypeFilter());
    filters.add(new LidnummerFilter(messageList, members));
    filters.add(new BedragFilter(members));
  }

  public boolean filter(IngBooking booking) {
    for (Filter filter : filters) {
      if (!filter.doFilter(booking)) {
        return false;
      }
    }
    return true;
  }

}
