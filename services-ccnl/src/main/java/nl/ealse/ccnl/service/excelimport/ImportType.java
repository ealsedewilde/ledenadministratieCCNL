package nl.ealse.ccnl.service.excelimport;

import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.excelimport.ProcessType;

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
    switch (id) {
      case "add":
        return ImportType.ADD;
      case "addReplace":
        return ImportType.ADD_OVERWRITE;
      default:
        return ImportType.REPLACE;
    }
  }

}
