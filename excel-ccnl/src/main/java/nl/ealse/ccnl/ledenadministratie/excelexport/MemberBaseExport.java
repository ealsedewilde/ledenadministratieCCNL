package nl.ealse.ccnl.ledenadministratie.excelexport;

import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.MemberBase;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;

@UtilityClass
public class MemberBaseExport {

  void addMember(CCNLBestand targetFile, MemberBase member) {
    MemberBaseAddressExport.addMember(targetFile, member);
    targetFile.addCell(member.getTelephoneNumber(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.TELEFOON));
    targetFile.addCell(member.getIbanNumber(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.IBAN_NUMMER));
    targetFile.addCell(DateUtil.toDate(member.getMemberSince()),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.LID_VANAF));
    targetFile.addCell(DateUtil.toDate(member.getModificationDate()),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.MUTATIEDATUM));
    targetFile.addCell(getPaymentMethodCode(member.getPaymentMethod()),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.INCASSO));
    String betaald = member.isCurrentYearPaid() ? CCNLColumnProperties.getPropertyHeeftBetaald()
        : CCNLColumnProperties.getPropertyNietBetaald();
    targetFile.addCell(betaald, CCNLColumnProperties.getKolomnummer(LidColumnDefinition.HEEFT_BETAALD));
    targetFile.addCell(member.getPaymentInfo(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.BETAAL_INFO));
    targetFile.addCell(DateUtil.toDate(member.getPaymentDate()),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.BETAALDATUM));
    String pas = member.isMembercardIssued() ? CCNLColumnProperties.getPropertyPasVerstuurd() : "";
    targetFile.addCell(pas, CCNLColumnProperties.getKolomnummer(LidColumnDefinition.PAS_VERSTUURD));
    targetFile.addCell(member.getEmail(), CCNLColumnProperties.getKolomnummer(LidColumnDefinition.EMAIL));
    targetFile.addCell(member.getMemberInfo(),
        CCNLColumnProperties.getKolomnummer(LidColumnDefinition.OPMERKING));
  }

  private String getPaymentMethodCode(PaymentMethod paymentMethod) {
    return switch (paymentMethod) {
      case BANK_TRANSFER -> CCNLColumnProperties.getPropertyOverschrijving();
      case DIRECT_DEBIT -> CCNLColumnProperties.getPropertyAutomatischeIncasso();
      case NOT_APPLICABLE -> CCNLColumnProperties.getPropertyErelid();
      case UNKNOWN -> "";
      default -> "";
    };
  }


}
