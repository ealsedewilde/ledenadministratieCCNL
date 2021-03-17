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

  public void addInvalidAddressMember(CCNLBestand targetFile, MemberBase member) {
    addMember(targetFile, member, false);
  }

  public void addMember(CCNLBestand targetFile, MemberBase member) {
    addMember(targetFile, member, true);
  }

  void addMember(CCNLBestand targetFile, MemberBase member, boolean valid) {
    if (member.getAddress().isAddressInvalid() != valid) {
      CCNLColumnProperties properties = targetFile.getProperties();
      MemberBaseAddressExport.addMember(targetFile, member, valid);
      targetFile.addCell(member.getTelephoneNumber(),
          properties.getKolomnummer(LidColumnDefinition.TELEFOON));
      targetFile.addCell(member.getIbanNumber(),
          properties.getKolomnummer(LidColumnDefinition.IBAN_NUMMER));
      targetFile.addCell(DateUtil.toDate(member.getMemberSince()),
          properties.getKolomnummer(LidColumnDefinition.LID_VANAF));
      targetFile.addCell(DateUtil.toDate(member.getModificationDate()),
          properties.getKolomnummer(LidColumnDefinition.MUTATIEDATUM));
      targetFile.addCell(getPaymentMethodCode(member.getPaymentMethod(), properties),
          properties.getKolomnummer(LidColumnDefinition.INCASSO));
      String betaald = member.isCurrentYearPaid() ? properties.getPropertyHeeftBetaald()
          : properties.getPropertyNietBetaald();
      targetFile.addCell(betaald, properties.getKolomnummer(LidColumnDefinition.HEEFT_BETAALD));
      targetFile.addCell(member.getPaymentInfo(),
          properties.getKolomnummer(LidColumnDefinition.BETAAL_INFO));
      targetFile.addCell(DateUtil.toDate(member.getPaymentDate()),
          properties.getKolomnummer(LidColumnDefinition.BETAALDATUM));
      String pas = member.isMembercardIssued() ? properties.getPropertyPasVerstuurd() : "";
      targetFile.addCell(pas, properties.getKolomnummer(LidColumnDefinition.PAS_VERSTUURD));
      targetFile.addCell(member.getEmail(), properties.getKolomnummer(LidColumnDefinition.EMAIL));
      targetFile.addCell(member.getMemberInfo(),
          properties.getKolomnummer(LidColumnDefinition.OPMERKING));
    }
  }

  private String getPaymentMethodCode(PaymentMethod paymentMethod,
      CCNLColumnProperties properties) {
    switch (paymentMethod) {
      case BANK_TRANSFER:
        return properties.getPropertyOverschrijving();
      case DIRECT_DEBIT:
        return properties.getPropertyAutomatischeIncasso();
      case NOT_APPLICABLE:
        return properties.getPropertyErelid();
      case UNKNOWN:
      default:
        return "";
    }
  }


}
