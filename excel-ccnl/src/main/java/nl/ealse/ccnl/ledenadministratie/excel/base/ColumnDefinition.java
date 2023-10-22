package nl.ealse.ccnl.ledenadministratie.excel.base;

/**
 * Marker interface
 *
 * @author Ealse
 */
public interface ColumnDefinition {

  enum Type {
    BOOLEAN, NUMBER, DATE, TIMESTAMP, STRING
  }

  String name();

  String heading();

  int ordinal();

  Type type();
}
