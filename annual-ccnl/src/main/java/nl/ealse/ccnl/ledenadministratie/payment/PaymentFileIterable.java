package nl.ealse.ccnl.ledenadministratie.payment;

import java.util.Iterator;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;

public class PaymentFileIterable implements Iterable<IngBooking> {

  private final PaymentFile paymentFile;

  public PaymentFileIterable(PaymentFile paymentFile) {
    this.paymentFile = paymentFile;
  }

  @Override
  public Iterator<IngBooking> iterator() {
    return new PaymentFileIterator(paymentFile);
  }

}
