package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.PaymentFileIterable;

public class FilterTestBase {

  protected Iterator<IngBooking> init() {
    PaymentFile pf = new PaymentFile();
    URL url = getClass().getResource("/booking.xml");
    pf.setFileName(url.getFile());
    try (InputStream is = url.openStream()) {
      String xml = new String(is.readAllBytes());
      pf.setXml(xml);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new PaymentFileIterable(pf).iterator();

  }

}
