package nl.ealse.ccnl.ledenadministratie.excel.lid;

import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;

/**
 * De kolom definities van het ledenbestand
 * 
 * @author Ealse
 *
 */
public enum LidColumnDefinition implements ColumnDefinition {
  LIDNUMMER(Type.NUMBER), VOORLETTERS(Type.STRING), TUSSENVOEGSEL(Type.STRING), ACHTERNAAM(
      Type.STRING), TELEFOON(Type.STRING), IBAN_NUMMER(Type.STRING), LID_VANAF(
          Type.DATE), MUTATIEDATUM(Type.DATE), INCASSO(Type.STRING), BETAAL_INFO(
              Type.STRING), HEEFT_BETAALD(Type.STRING), PAS_VERSTUURD(Type.STRING), BETAALDATUM(
                  Type.DATE), EMAIL(
                      Type.STRING), OPMERKING(Type.STRING), REDEN_OPZEGGING(Type.STRING);

  private final Type type;

  private LidColumnDefinition(Type type) {
    this.type = type;
  }

  public enum Property {
    // betekenis voor speciale kolommen
    INCASSO_AUTOMATISCH, INCASSO_EENMALIG, INCASSO_ERELID, INCASSO_OVERSCHRIJVING, HEEFT_BETAALD_JA, HEEFT_BETAALD_NEE, PAS_VERSTUURD_JA, PAS_VERSTUURD_NEE;
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
