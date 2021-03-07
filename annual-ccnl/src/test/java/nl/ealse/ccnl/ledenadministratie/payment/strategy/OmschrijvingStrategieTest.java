package nl.ealse.ccnl.ledenadministratie.payment.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.filter.FilterTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OmschrijvingStrategieTest extends FilterTestBase {
  
  @Test
  void omschrijvingStrategyTest() {
    List<Integer> nummers = new ArrayList<>();
    nummers.add(1507);
    OmschrijvingStrategie sut = new OmschrijvingStrategie();
    Iterator<IngBooking> itr = init();
    IngBooking b = itr.next();
    sut.bepaalLidnummer(b);
    Assertions.assertEquals(1507, b.getLidnummer());
  }


}
