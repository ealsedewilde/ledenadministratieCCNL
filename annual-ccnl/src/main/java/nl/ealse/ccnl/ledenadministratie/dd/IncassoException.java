package nl.ealse.ccnl.ledenadministratie.dd;

@SuppressWarnings("serial")
public class IncassoException extends Exception {

  public IncassoException(String message) {
    super(message);
  }

  public IncassoException(String message, Throwable cause) {
    super(message, cause);
  }

}
