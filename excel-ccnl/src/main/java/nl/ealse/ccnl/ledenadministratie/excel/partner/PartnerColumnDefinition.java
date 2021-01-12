package nl.ealse.ccnl.ledenadministratie.excel.partner;

import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;

public enum PartnerColumnDefinition implements ColumnDefinition {
  PARTNER_NUMMER(Type.NUMBER), PARTNER_NAAM(Type.STRING), PARTNER_AANHEF(
      Type.STRING), PARTNER_CONTACTPERSOON(Type.STRING), PARTNER_TELEFOON(
          Type.STRING), PARTNER_IBAN(Type.STRING), PARTNER_VANAF(Type.DATE), PARTNER_MUTATIEDATUM(
              Type.DATE), PARTNER_EMAIL(Type.STRING), PARTNER_OPMERKING(Type.STRING);

  private final Type type;

  private PartnerColumnDefinition(Type type) {
    this.type = type;
  }

  @Override
  public Type type() {
    return type;
  }


  public String heading() {
    return name().substring(8).toLowerCase();
  }


}
