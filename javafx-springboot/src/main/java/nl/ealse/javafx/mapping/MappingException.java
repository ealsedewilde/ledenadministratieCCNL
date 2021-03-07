package nl.ealse.javafx.mapping;

@SuppressWarnings("serial")
public class MappingException extends RuntimeException {

  public MappingException(String message) {
    super(message);
  }

  public MappingException(String message, Throwable t) {
    super(message, t);
  }

}
