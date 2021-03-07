package nl.ealse.ccnl.control.exception;

@SuppressWarnings("serial")
public class PDFViewerException extends RuntimeException {

  public PDFViewerException() {}

  public PDFViewerException(String message) {
    super(message);
  }

  public PDFViewerException(Throwable cause) {
    super(cause);
  }

  public PDFViewerException(String message, Throwable cause) {
    super(message, cause);
  }

  public PDFViewerException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
