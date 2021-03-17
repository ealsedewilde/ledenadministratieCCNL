package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;

@UtilityClass
public class ArchivedMemberExport {

  public void addMember(CCNLBestand targetFile, ArchivedMember member) {
    MemberBaseExport.addMember(targetFile, member.getMember(), true);
    targetFile.addCell(member.getId().getMemberNumber(),
        targetFile.getProperties().getKolomnummer(LidColumnDefinition.LIDNUMMER));
  }

}
