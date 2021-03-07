package nl.ealse.ccnl.validation;

public interface ContentValidator {

  void validate();

  void reset();

  void initialize();

  boolean isValid();

}
