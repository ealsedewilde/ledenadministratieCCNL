package nl.ealse.ccnl.ledenadministratie.excel.base;

public enum AdresColumnDefinition implements ColumnDefinition {
  STRAAT_HUISNUMMER(Type.STRING), POSTCODE(Type.STRING), WOONPLAATS(Type.STRING), LAND(Type.STRING);

  private final Type type;

  private AdresColumnDefinition(Type type) {
    this.type = type;
  }

  @Override
  public Type type() {
    return type;
  }

  @Override
  public String heading() {
    return name().toLowerCase();
  }

}
