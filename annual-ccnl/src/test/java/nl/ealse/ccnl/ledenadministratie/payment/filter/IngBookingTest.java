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
    bk.setLidnummer(9999);
    String s = bk.toString();
    int ix = s.indexOf("<NtryRef>032020034543595411000000002</NtryRef>");
    Assertions.assertTrue(ix > -1);
    
    IngBooking bk2 = itr.next();
    bk2.setLidnummer(9999);
    int r = bk.compareTo(bk2);
    Assertions.assertEquals(0, r);
    Assertions.assertEquals(true, bk.equals(bk2));
    
    IngBooking storno = itr.next();
    
    
    String postcode = storno.getPostcode();
    Assertions.assertEquals("", postcode);
    
    String info = storno.getStorneringInfo();
    Assertions.assertEquals("lid 9999", info);

    CancelReason cr = storno.getStornoReden();
    Assertions.assertEquals(CancelReason.AC06, cr);
  }

}
