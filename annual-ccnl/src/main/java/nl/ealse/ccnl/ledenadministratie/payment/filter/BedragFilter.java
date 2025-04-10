package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.MemberShipFee;

@Slf4j
public class BedragFilter implements Filter {
  
  private final Map<Integer, Member> members;
  
  public BedragFilter(List<Member> members) {
    this.members = members.stream().collect(Collectors.toMap(Member::getMemberNumber, member -> member));
  }

  @Override
  public boolean doFilter(IngBooking booking) {
    
    double bedrag = booking.getBedrag();
    if (bedrag < 0) {
      bedrag = bedrag * -1;
    }
    
   if (bedrag >= MemberShipFee.getIncasso() && bedrag <= MemberShipFee.getOverboeken()) {
      return true;
    } else {
      Member member = members.get(booking.getLidnummer());
      if (member == null) {
        log.info("member not found for " + booking.getLidnummer());
        return false;
      }
      return true;
    }
  }

}
