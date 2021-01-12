package nl.ealse.ccnl.ledenadministratie.excel.file;

import java.util.EnumMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.model.MemberBase;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.util.DateUtil;

@UtilityClass
public class MemberBaseExport {

  private static final Map<PaymentMethod, String> paymentmethods =
      new EnumMap<>(PaymentMethod.class);

  static {
    paymentmethods.put(PaymentMethod.BANK_TRANSFER, "A");
    paymentmethods.put(PaymentMethod.DIRECT_DEBIT, "I");
    paymentmethods.put(PaymentMethod.NOT_APPLICABLE, "E");
    paymentmethods.put(PaymentMethod.UNKNOWN, "?");
  }

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
      String betaald = member.isCurrentYearPaid() ? "J" : "N";
      targetFile.addCell(betaald, properties.getKolomnummer(LidColumnDefinition.HEEFT_BETAALD));
      targetFile.addCell(member.getPaymentInfo(),
          properties.getKolomnummer(LidColumnDefinition.BETAAL_INFO));
      targetFile.addCell(DateUtil.toDate(member.getPaymentDate()),
          properties.getKolomnummer(LidColumnDefinition.BETAALDATUM));
      String pas = member.isMembercardIssued() ? "J" : "";
      targetFile.addCell(pas, properties.getKolomnummer(LidColumnDefinition.PAS_VERSTUURD));
      targetFile.addCell(member.getEmail(), properties.getKolomnummer(LidColumnDefinition.EMAIL));
      targetFile.addCell(member.getMemberInfo(),
          properties.getKolomnummer(LidColumnDefinition.OPMERKING));
    }
  }

}
