package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.util.Iterator;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OmschrijvingFilterTest extends FilterTestBase {

  @Test
  void filtertest() {
    OmschrijvingFilter f = new OmschrijvingFilter();
    Iterator<IngBooking> itr = init();
    IngBooking b = itr.next();
    f.doFilter(b);
    Assertions.assertTrue(b.isContributie());
    b = itr.next();
    f.doFilter(b);
    Assertions.assertFalse(b.isContributie());
  }


}
