package nl.ealse.ccnl.ledenadministratie.excel.lid;

import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;

/**
 * De kolom definities van het ledenbestand
 *
 * @author Ealse
 *
 */
public enum MagazineColumnDefinition implements ColumnDefinition {
  LIDNUMMER(Type.NUMBER), VOORLETTERS(Type.STRING), TUSSENVOEGSEL(Type.STRING), ACHTERNAAM(
      Type.STRING), PAS_MEESTUREN(Type.STRING);

  private final Type type;

  private MagazineColumnDefinition(Type type) {
    this.type = type;
  }

  public enum Property {
    // betekenis voor speciale kolommen
    PAS_MEESTUREN_JA, PAS_MEESTUREN_NEE;
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
