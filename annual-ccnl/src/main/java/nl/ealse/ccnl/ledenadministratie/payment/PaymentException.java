package nl.ealse.ccnl.ledenadministratie.payment;

@SuppressWarnings("serial")
public class PaymentException extends RuntimeException {

  public PaymentException(Throwable cause) {
    super(cause);
  }

  public PaymentException(String message, Throwable cause) {
    super(message, cause);
  }

}
