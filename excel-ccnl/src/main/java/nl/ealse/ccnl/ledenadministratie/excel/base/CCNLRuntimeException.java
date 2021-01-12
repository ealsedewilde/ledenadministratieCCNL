package nl.ealse.ccnl.ledenadministratie.excel.base;

@SuppressWarnings("serial")
public class CCNLRuntimeException extends RuntimeException {

  public CCNLRuntimeException(Throwable cause) {
    super(cause);
  }

  public CCNLRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
