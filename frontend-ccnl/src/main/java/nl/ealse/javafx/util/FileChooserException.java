package nl.ealse.javafx.util;

/**
 * Thrown when the initial directory of a FileChooser is invalid.
 */
public class FileChooserException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Signal the invalid directory.
   *
   * @param message - the error message
   */
  public FileChooserException(String message) {
    super(message);
  }

 
}
