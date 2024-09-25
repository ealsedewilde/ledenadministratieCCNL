package nl.ealse.ccnl.ledenadministratie.excelimport;

import lombok.Getter;

public enum ImportType {
  ADD(ProcessType.ADD),

  ADD_OVERWRITE(ProcessType.ADD_OVERWRITE),

  REPLACE(ProcessType.REPLACE);

  @Getter
  private final ProcessType processType;

  private ImportType(ProcessType processType) {
    this.processType = processType;
  }

  public static ImportType fromId(String id) {
    return switch (id) {
      case "add" -> ImportType.ADD;
      case "addReplace" -> ImportType.ADD_OVERWRITE;
      default -> ImportType.REPLACE;
    };
  }

}
