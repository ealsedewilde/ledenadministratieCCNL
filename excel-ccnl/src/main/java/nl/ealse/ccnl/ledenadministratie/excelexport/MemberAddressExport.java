package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.Member;

@UtilityClass
public class MemberAddressExport {

  public void addMember(CCNLBestand targetFile, Member member) {
    MemberBaseAddressExport.addMember(targetFile, member);
    targetFile.addCell(member.getMemberNumber(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.LIDNUMMER));
  }

}
