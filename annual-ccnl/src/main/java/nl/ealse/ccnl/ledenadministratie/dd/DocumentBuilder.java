package nl.ealse.ccnl.ledenadministratie.dd;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import nl.ealse.ccnl.ledenadministratie.dd.model.CustomerDirectDebitInitiationV02;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.GroupHeader39;
import nl.ealse.ccnl.ledenadministratie.dd.model.PartyIdentification32;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;

/**
 * Het SEPA-INCASSO document opbouwen.
 * 
 * @author Ealse
 *
 */
public class DocumentBuilder {

  /**
   * Het op te bouwen object
   */
  private Document document = new Document();

  public DocumentBuilder(IncassoProperties properties) {
    init(properties);
  }

  /**
   * Aanmaakdatum vullen
   * 
   * @param CreDtTm
   * @return
   */
  public DocumentBuilder metCreateDate(LocalDateTime creDtTm) {
    document.getCstmrDrctDbtInitn().getGrpHdr().setCreDtTm(DateUtil.toXMLDateTime(creDtTm));
    return this;
  }

  /**
   * (Optioneel) Som van alle te incasseren bedragen.
   * 
   * @param ctrlSum
   * @return
   */
  public DocumentBuilder metControlSum(BigDecimal ctrlSum) {
    document.getCstmrDrctDbtInitn().getGrpHdr().setCtrlSum(ctrlSum);
    return this;
  }

  /**
   * MessageId (maximaal 35 tekens)
   * 
   * @param NbOfTxs
   * @return
   */
  public DocumentBuilder metMessageId(String messageId) {
    document.getCstmrDrctDbtInitn().getGrpHdr().setMsgId(messageId);
    return this;
  }

  /**
   * Totaal aantal transacties
   * 
   * @param NbOfTxs
   * @return
   */
  public DocumentBuilder metNumberOfTransactions(Integer nbOfTxs) {
    document.getCstmrDrctDbtInitn().getGrpHdr().setNbOfTxs(nbOfTxs.toString());
    return this;
  }

  /**
   * Crediteur gedeelte toevoegen.
   * 
   * @param paymentInstruction
   * @return
   */
  public DocumentBuilder metPaymentInstructionInformationBuilder(
      PaymentInstructionInformation4 paymentInstruction) {
    document.getCstmrDrctDbtInitn().getPmtInf().add(paymentInstruction);
    return this;
  }

  public Document build() {
    return document;
  }

  /**
   * Vaste gegevens initiëren.
   */
  private void init(IncassoProperties properties) {
    CustomerDirectDebitInitiationV02 customerDirectDebitInitiationV02 =
        new CustomerDirectDebitInitiationV02();
    document.setCstmrDrctDbtInitn(customerDirectDebitInitiationV02);

    GroupHeader39 groupHeader39 = new GroupHeader39();
    customerDirectDebitInitiationV02.setGrpHdr(groupHeader39);
    PartyIdentification32 initiatingParty = new PartyIdentification32();
    initiatingParty.setNm(properties.getNaam());
    groupHeader39.setInitgPty(initiatingParty);
  }

}

