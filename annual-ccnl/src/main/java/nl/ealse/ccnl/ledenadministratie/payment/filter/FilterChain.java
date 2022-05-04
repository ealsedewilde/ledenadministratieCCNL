package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.MemberShipFee;

public final class FilterChain {
  private final List<Filter> filters = new ArrayList<>();
  private final PeildatumFilter peildatumFilter;

  public FilterChain(List<Member> members, LocalDate referenceDate, MemberShipFee memberShipFee) {
    this.peildatumFilter = new PeildatumFilter(referenceDate);
    filters.add(new BoekingTypeFilter());
    filters.add(new BedragFilter(memberShipFee));
    filters.add(new LidnummerFilter(members));
  }

  public boolean filter(IngBooking booking) {
    if (peildatumFilter.doFilter(booking)) {
      for (Filter filter : filters) {
        if (!filter.doFilter(booking)) {
          return false;
        }
      }
    }
    return true;
  }

}
