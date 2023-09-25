package nl.ealse.ccnl.validation;

/**
 * All validators must implement this interface.
 */
public interface ContentValidator {

  void validate();

  void reset();

  void initialize();

  boolean isValid();

}
