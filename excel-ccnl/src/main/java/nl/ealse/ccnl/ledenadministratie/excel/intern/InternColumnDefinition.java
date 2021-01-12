package nl.ealse.ccnl.ledenadministratie.excel.intern;

import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;

public enum InternColumnDefinition implements ColumnDefinition {
  INTERN_NUMMER(Type.NUMBER), INTERN_PREFIX(Type.STRING), INTERN_AANHEF(
      Type.STRING), INTERN_FUNCTIE(Type.STRING), INTERN_TELEFOON(
          Type.STRING), INTERN_MUTATIEDATUM(Type.DATE), INTERN_CONTACTPERSOON(Type.STRING);

  private final Type type;

  private InternColumnDefinition(Type type) {
    this.type = type;
  }

  @Override
  public Type type() {
    return type;
  }

  public String heading() {
    return name().substring(7).toLowerCase();
  }

}
