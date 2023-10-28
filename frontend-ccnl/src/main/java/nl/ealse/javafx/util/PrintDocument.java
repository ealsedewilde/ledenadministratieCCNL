package nl.ealse.javafx.util;

public interface PrintDocument {

  Object getDocument();

  Type getType();

  enum Type {
    PDF, IMAGE
  }
}
