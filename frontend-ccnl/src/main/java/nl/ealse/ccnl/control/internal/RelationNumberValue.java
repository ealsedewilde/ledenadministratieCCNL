package nl.ealse.ccnl.control.internal;

import lombok.Getter;

enum RelationNumberValue {
  VOORZITTER("Voorzitter", 8080),

  SECRETARIAAT("Secretariaat", 8081),

  PENNINGMEESTER("Penningmeester", 8082),

  EVENEMENTEN("Evenementen", 8083),

  ALGEMEEN("Algemeen", 8084),

  REDACTIE("Redactie", 8085),

  LEDENADMINISTRATIE("Ledenadministratie", 8086),

  MAGAZIJN("Magazijn", 8087);

  @Getter
  private final String label;

  @Getter
  private final int relationNumber;

  private RelationNumberValue(String label, int relationNumber) {
    this.label = label;
    this.relationNumber = relationNumber;
  }

  public static RelationNumberValue fromLabel(String label) {
    for (RelationNumberValue rn : RelationNumberValue.values()) {
      if (rn.label.equals(label)) {
        return rn;
      }
    }
    throw new IllegalArgumentException(label);
  }
}