package nl.ealse.ccnl.ledenadministratie.dd;

@SuppressWarnings("serial")
public class InvalidIbanException extends Exception {

  public InvalidIbanException() {}

  public InvalidIbanException(String message) {
    super(message);
  }

  public InvalidIbanException(Throwable cause) {
    super(cause);
  }

  public InvalidIbanException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidIbanException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
