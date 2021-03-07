package nl.ealse.javafx;

import lombok.Getter;

/**
 * Thrown when referencing a missing page.
 * 
 * @author ealse
 *
 */
@SuppressWarnings("serial")
public class FXMLMissingException extends Exception {

  @Getter
  private final String pagekey;

  public FXMLMissingException(String message, String pagekey) {
    super(message);
    this.pagekey = pagekey;
  }

}
