package nl.ealse.ccnl.ledenadministratie.excel.base;

import lombok.Getter;

public enum SheetDefinition {
  LEDEN(Type.LID),

  NIEUWE_LEDEN(Type.LID),

  CLUBS(Type.CLUB),

  RELATIES(Type.PARTNER),

  INTERN(Type.INTERN),

  OPZEGGERS(Type.LID),

  RETOUR(Type.LID),

  NIET_BETAALD(Type.LID),

  DUBBEL_BETAALD(Type.LID),

  VREEMD(Type.LID),

  OPZEGGEN_VOLGEND_JAAR(Type.LID);

  @Getter
  private final Type type;

  private SheetDefinition(Type type) {
    this.type = type;
  }

  public enum Type {
    LID, PARTNER, CLUB, INTERN
  }
}
