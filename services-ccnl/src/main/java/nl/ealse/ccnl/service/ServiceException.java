package nl.ealse.ccnl.service;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

  public ServiceException(String message, Throwable e) {
    super(message, e);
  }

}
