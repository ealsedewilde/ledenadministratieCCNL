package nl.ealse.javafx.print;

public interface PrintDocument {

  Object getDocument();

  Type getType();

  enum Type {
    PDF, IMAGE
  }
}
