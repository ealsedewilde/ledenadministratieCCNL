package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.Member;

@UtilityClass
public class MemberExport {

  public void addMember(CCNLBestand targetFile, Member member) {
    MemberBaseExport.addMember(targetFile, member);
    targetFile.addCell(member.getMemberNumber(),
        targetFile.getProperties().getKolomnummer(LidColumnDefinition.LIDNUMMER));
  }

}
