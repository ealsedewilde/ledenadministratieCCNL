package nl.ealse.ccnl.ledenadministratie.payment.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import nl.ealse.ccnl.ledenadministratie.payment.IngBooking;
import nl.ealse.ccnl.ledenadministratie.payment.PaymentFileIterable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FilterTestBase {

  protected Iterator<IngBooking> init() {
    PaymentFile pf = new PaymentFile();
    Resource r = new ClassPathResource("booking.xml");
    pf.setFileName(r.getFilename());
    try (InputStream is = r.getInputStream()) {
      String xml = new String(is.readAllBytes());
      pf.setXml(xml);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new PaymentFileIterable(pf).iterator();

  }

}
