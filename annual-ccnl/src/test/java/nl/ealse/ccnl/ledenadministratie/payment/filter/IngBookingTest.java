package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.util.Iterator;
import nl.ealse.ccnl.ledenadministratie.payment.CancelReason;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IngBookingTest extends FilterTestBase {

  @Test
  void testBooking() {
    Iterator<IngBooking> itr = init();
    IngBooking bk = itr.next();
    String s = bk.toString();
    int ix = s.indexOf("<NtryRef>032020034543595411000000002</NtryRef>");
    Assertions.assertTrue(ix > -1);
    boolean b = bk.equals(bk);
    Assertions.assertTrue(b);
    boolean ok = bk.equals(bk);
    Assertions.assertTrue(ok);
    bk.hashCode();
    Assertions.assertEquals(0, bk.compareTo(bk));
    
    CancelReason cr = CancelReason.valueOf("SL01");
    Assertions.assertEquals(CancelReason.SL01, cr);
  }

}
