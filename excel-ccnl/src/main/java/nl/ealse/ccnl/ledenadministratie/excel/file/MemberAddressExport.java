package nl.ealse.ccnl.ledenadministratie.excel.file;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.Member;

@UtilityClass
public class MemberAddressExport {

  public void addMember(CCNLBestand targetFile, Member member) {
    addMember(targetFile, member, true);
  }

  public void addMember(CCNLBestand targetFile, Member member, boolean valid) {
    if (member.getAddress().isAddressInvalid() != valid) {
      MemberBaseAddressExport.addMember(targetFile, member, valid);
      targetFile.addCell(member.getMemberNumber(),
          targetFile.getProperties().getKolomnummer(LidColumnDefinition.LIDNUMMER));
    }
  }

}
