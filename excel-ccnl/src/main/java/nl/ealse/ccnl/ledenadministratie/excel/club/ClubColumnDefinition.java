package nl.ealse.ccnl.ledenadministratie.excel.club;

import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;

public enum ClubColumnDefinition implements ColumnDefinition {
  CLUB_NUMMER(Type.NUMBER), CLUB_NAAM(Type.STRING), CLUB_AANHEF(Type.STRING), CLUB_CONTACTPERSOON(
      Type.STRING);

  private final Type type;

  private ClubColumnDefinition(Type type) {
    this.type = type;
  }

  @Override
  public Type type() {
    return type;
  }

  public String heading() {
    return name().substring(5).toLowerCase();
  }

}
