package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransactionInformation9;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.excel.Ledenbestand;
import nl.ealse.ccnl.ledenadministratie.model.Member;

@Slf4j
public class SepaIncassoDocumentGenerator {

  private final IncassoProperties incassoProperties;
  private final CCNLColumnProperties excelProperties;
  private final DocumentBuilder documentBuilder;
  private final PaymentInstructionInformationBuilder paymentInstructionInformationBuilder;

  private int aantalTransacties = 0;
  private double somTransactieBedrag = 0.0;


  public SepaIncassoDocumentGenerator(IncassoProperties incassoProperties,
      CCNLColumnProperties excelProperties) {
    this.incassoProperties = incassoProperties;
    this.excelProperties = excelProperties;
    this.documentBuilder = new DocumentBuilder(incassoProperties);
    this.paymentInstructionInformationBuilder =
        new PaymentInstructionInformationBuilder(incassoProperties);
  }

  public Document generateIncassoDocument(File controlFile, List<Member> members)
      throws IncassoException {
    try (Ledenbestand incassobestand = new Ledenbestand(controlFile, excelProperties)) {
      incassobestand.addMemberHeading();
      for (Member member : members) {
        updateMember(member);
        incassobestand.addMember(member);
        if (incassoProperties.isTest() && aantalTransacties > 10) {
          break;
        }
      }
      PaymentInstructionInformation4 paymentInstruction =
          paymentInstructionInformationBuilder.metAantalTransacties(aantalTransacties)
              .metSomTransactieBedrag(BigDecimal.valueOf(somTransactieBedrag))
              .metIncassodatum(incassoProperties.getIncassoDatum()).build();
      return documentBuilder.metControlSum(BigDecimal.valueOf(somTransactieBedrag))
          .metCreateDate(LocalDateTime.now()).metMessageId(incassoProperties.getMessageId())
          .metNumberOfTransactions(aantalTransacties)
          .metPaymentInstructionInformationBuilder(paymentInstruction).build();
    } catch (IOException e) {
      log.error("Fout bij aanmaken incassobestand document", e);
      throw new IncassoException("Fout bij aanmaken incassobestand document", e);
    }
  }

  private void updateMember(Member member) {
    try {
      DirectDebitTransactionInformation9 transactie = getDebitTransaction(member);
      paymentInstructionInformationBuilder.toevoegenDebitTransactie(transactie);
      member.setCurrentYearPaid(true);
      member.setMemberInfo("INCASSO OK");
      aantalTransacties++;
      somTransactieBedrag += incassoProperties.getIncassoBedrag().doubleValue();
    } catch (IllegalArgumentException e) {
      member.setMemberInfo(e.getMessage());
    }
  }

  /**
   * Debit kant van een INCASSO aanmaken.
   * 
   * @param lid
   * @return
   */
  private DirectDebitTransactionInformation9 getDebitTransaction(Member member) {
    DirectDebitTransactionInformationBuilder builder =
        new DirectDebitTransactionInformationBuilder(incassoProperties);
    String iban = member.getIbanNumber();
    if (iban == null) {
      throw new IllegalArgumentException("Geen IBAN bij lid: " + member.getMemberNumber());
    }
    try {
      return builder.metDibiteurIBAN(iban.trim()).metDibiteurNaam(member.getFullName())
          .metLidnummer(member.getMemberNumber()).build();
    } catch (InvalidIbanException e) {
      throw new IllegalArgumentException(e.getMessage() + " bij lid " + member.getMemberNumber());
    }
  }

}
