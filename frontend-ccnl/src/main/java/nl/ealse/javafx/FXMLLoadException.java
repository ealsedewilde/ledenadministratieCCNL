package nl.ealse.javafx;

import java.io.IOException;

/**
 * Thrown when loading FXML throws an {@link IOException}.
 *
 * @author ealse
 *
 */
@SuppressWarnings("serial")
public class FXMLLoadException extends RuntimeException {

  public FXMLLoadException(String msg) {
    super(msg);
  }

  public FXMLLoadException(String msg, Throwable t) {
    super(msg, t);
  }

}
