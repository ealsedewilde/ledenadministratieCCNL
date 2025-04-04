package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransactionInformation9;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;
import nl.ealse.ccnl.ledenadministratie.excel.Ledenbestand;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Generate the Direct Debit document.
 */
@Slf4j
public class SepaIncassoDocumentGenerator {

  private final SepaIncassoContext context;
  private final DocumentBuilder documentBuilder;
  private final PaymentInstructionInformationBuilder paymentInstructionInformationBuilder;

  @Getter
  private Document document;

  @Getter
  private int aantalTransacties = 0;
  private double somTransactieBedrag = 0.0;


  /**
   * Create the generator.
   *
   * @param context - all data neede to generate the document
   */
  public SepaIncassoDocumentGenerator(SepaIncassoContext context) {
    this.context = context;
    this.documentBuilder = new DocumentBuilder();
    this.paymentInstructionInformationBuilder = new PaymentInstructionInformationBuilder();
  }

  /**
   * Generate the Direct Debit document. This method updates the payment status of members.
   *
   * @throws IncassoException - whenever document generation fails
   */
  public void generateIncassoDocument() throws IncassoException {
    try (Ledenbestand incassobestand = new Ledenbestand(context.controlExcelFile())) {
      incassobestand.addMemberHeading();
      for (Member member : context.members()) {
        // when every grant is added convert to a check
        String info = context.sepaNumbers().contains(member.getMemberNumber()) ? "SEPA Machtiging: Ja"
            : "SEPA Machtiging: Nee";
        member.setPaymentInfo(info);
        incassobestand.addMember(member);

        updateMember(member);
        if (IncassoProperties.isTest() && aantalTransacties > 9) {
          break;
        }
      }
      PaymentInstructionInformation4 paymentInstruction =
          paymentInstructionInformationBuilder.metAantalTransacties(aantalTransacties)
              .metSomTransactieBedrag(BigDecimal.valueOf(somTransactieBedrag))
              .metIncassodatum(IncassoProperties.getIncassoDatum()).build();
      document = documentBuilder.metControlSum(BigDecimal.valueOf(somTransactieBedrag))
          .metCreateDate(LocalDateTime.now()).metMessageId(IncassoProperties.getMessageId())
          .metNumberOfTransactions(aantalTransacties)
          .metPaymentInstructionInformation(paymentInstruction).build();
    } catch (IOException e) {
      log.error("Fout bij aanmaken incassobestand document", e);
      throw new IncassoException("Fout bij aanmaken incassobestand document", e);
    }
  }

  private void updateMember(Member member) {
    try {
      DirectDebitTransactionInformation9 transactie = getDebitTransaction(member);
      paymentInstructionInformationBuilder.toevoegenDebitTransactie(transactie);
      member.setDirectDebitExecuted(true);
      member.setCurrentYearPaid(true);
      member.setPaymentInfo(IncassoProperties.getIncassoReden());
      member.setPaymentDate(IncassoProperties.getIncassoDatum());
      aantalTransacties++;
      somTransactieBedrag += IncassoProperties.getIncassoBedrag().doubleValue();
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
        new DirectDebitTransactionInformationBuilder();
    String iban = member.getIbanNumber();
    if (iban == null) {
      String msg = "Geen IBAN bij lid: " + member.getMemberNumber();
      context.messages().add(msg);
      throw new IllegalArgumentException(msg);
    }
    try {
      return builder.metDibiteurIBAN(iban.trim(), member.getBicCode())
          .metDibiteurNaam(member.getIbanOwnerName()).metLidnummer(member.getMemberNumber())
          .build();
    } catch (InvalidIbanException e) {
      String msg = "Geen geldige IBAN bij lid: " + member.getMemberNumber();
      context.messages().add(msg);
      throw new IllegalArgumentException(e.getMessage() + " bij lid " + member.getMemberNumber());
    }
  }

}
