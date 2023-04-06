package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.MemberShipFee;

public final class FilterChain {
  private final List<Filter> filters = new ArrayList<>();
  private final LidnummerFilter LidnummerFilter;

  public FilterChain(List<Member> members, LocalDate referenceDate, MemberShipFee memberShipFee) {
    LidnummerFilter = new LidnummerFilter(members);
    filters.add(new PeildatumFilter(referenceDate));
    filters.add(new BedragFilter(memberShipFee));
    filters.add(new BoekingTypeFilter());
  }

  public boolean filter(IngBooking booking) {
    for (Filter filter : filters) {
      if (!filter.doFilter(booking)) {
        return false;
      }
    }
    LidnummerFilter.doFilter(booking);
    return true;
  }

}
