package nl.ealse.ccnl.ledenadministratie.dd;

import java.math.BigDecimal;
import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.dd.model.AccountIdentification4Choice;
import nl.ealse.ccnl.ledenadministratie.dd.model.BranchAndFinancialInstitutionIdentification4;
import nl.ealse.ccnl.ledenadministratie.dd.model.CashAccount16;
import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransactionInformation9;
import nl.ealse.ccnl.ledenadministratie.dd.model.FinancialInstitutionIdentification7;
import nl.ealse.ccnl.ledenadministratie.dd.model.GenericPersonIdentification1;
import nl.ealse.ccnl.ledenadministratie.dd.model.LocalInstrument2Choice;
import nl.ealse.ccnl.ledenadministratie.dd.model.Party6Choice;
import nl.ealse.ccnl.ledenadministratie.dd.model.PartyIdentification32;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentInstructionInformation4;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentMethod2Code;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentTypeInformation20;
import nl.ealse.ccnl.ledenadministratie.dd.model.PersonIdentification5;
import nl.ealse.ccnl.ledenadministratie.dd.model.PersonIdentificationSchemeName1Choice;
import nl.ealse.ccnl.ledenadministratie.dd.model.SequenceType1Code;
import nl.ealse.ccnl.ledenadministratie.dd.model.ServiceLevel8Choice;
import nl.ealse.ccnl.ledenadministratie.excel.dd.BicResolver;


/**
 * Bouwen crediteur zijde van de INCASSO
 * 
 * @author Ealse
 *
 */
public class PaymentInstructionInformationBuilder {

  /**
   * Het op te bouwen object
   */
  private PaymentInstructionInformation4 paymentInstructionInformation =
      new PaymentInstructionInformation4();

  public PaymentInstructionInformationBuilder(IncassoProperties properties) {
    init(properties);
  }

  /**
   * Betaalinformatie toevoegen.
   * 
   * @param paymentInformation
   * @return
   */
  public PaymentInstructionInformationBuilder metPaymentInformation(String paymentInformation) {
    paymentInstructionInformation.setPmtInfId(paymentInformation);
    return this;
  }

  /**
   * Datum zetten waarop de INCASSO uitgevoerd moet worden.
   * 
   * @param incassodatum
   * @return
   */
  public PaymentInstructionInformationBuilder metIncassodatum(LocalDate incassodatum) {
    paymentInstructionInformation.setReqdColltnDt(DateUtil.toXMLDate(incassodatum));
    return this;
  }

  /**
   * Debit transactie toevoegen.
   * 
   * @param transactie
   * @return
   */
  public PaymentInstructionInformationBuilder toevoegenDebitTransactie(
      DirectDebitTransactionInformation9 transactie) {
    paymentInstructionInformation.getDrctDbtTxInf().add(transactie);
    return this;
  }

  /**
   * Het totaal aantal van transacties toevoegen.
   * 
   * @param aantalTransacties
   * @return
   */
  public PaymentInstructionInformationBuilder metAantalTransacties(Integer aantalTransacties) {
    paymentInstructionInformation.setNbOfTxs(aantalTransacties.toString());
    return this;
  }

  /**
   * Het totaalbedrag van alle transacties toevoegen.
   * 
   * @param somTransactieBedrag
   * @return
   */
  public PaymentInstructionInformationBuilder metSomTransactieBedrag(
      BigDecimal somTransactieBedrag) {
    paymentInstructionInformation.setCtrlSum(somTransactieBedrag);
    return this;
  }

  public PaymentInstructionInformation4 build() {
    return paymentInstructionInformation;
  }

  /**
   * Vaste gegevens initiëren.
   */
  private void init(IncassoProperties properties) {

    paymentInstructionInformation.setBtchBookg(true);
    paymentInstructionInformation.setPmtInfId(properties.getMessageId());
    paymentInstructionInformation.setPmtMtd(PaymentMethod2Code.DD);
    PaymentTypeInformation20 paymentTypeInformation20 = new PaymentTypeInformation20();
    ServiceLevel8Choice serviceLevel8Choice = new ServiceLevel8Choice();
    serviceLevel8Choice.setCd("SEPA");
    paymentTypeInformation20.setSvcLvl(serviceLevel8Choice);
    LocalInstrument2Choice localInstrument2Choice = new LocalInstrument2Choice();
    localInstrument2Choice.setCd("CORE");
    paymentTypeInformation20.setLclInstrm(localInstrument2Choice);
    // paymentTypeInformation20.setSeqTp(SequenceType1Code.OOFF); // eenmalige INCASSO
    paymentTypeInformation20.setSeqTp(SequenceType1Code.valueOf(properties.getMachtigingType()));
    // paymentTypeInformation20.setSeqTp(SequenceType1Code.FRST); // eerste van een serie
    // paymentTypeInformation20.setSeqTp(SequenceType1Code.RCUR); // herhaalde INCASSO
    paymentInstructionInformation.setPmtTpInf(paymentTypeInformation20);

    PartyIdentification32 creditor = new PartyIdentification32();
    creditor.setNm(properties.getNaam());
    paymentInstructionInformation.setCdtr(creditor);

    CashAccount16 ibanRekening = new CashAccount16();
    AccountIdentification4Choice ibanNummer = new AccountIdentification4Choice();
    ibanNummer.setIBAN(properties.getIbanNummer());
    ibanRekening.setId(ibanNummer);
    paymentInstructionInformation.setCdtrAcct(ibanRekening);

    BranchAndFinancialInstitutionIdentification4 bic =
        new BranchAndFinancialInstitutionIdentification4();
    FinancialInstitutionIdentification7 finId = new FinancialInstitutionIdentification7();
    finId.setBIC(BicResolver.getBicCode(properties.getIbanNummer()));
    bic.setFinInstnId(finId);
    paymentInstructionInformation.setCdtrAgt(bic);
    paymentInstructionInformation.setCdtrSchmeId(buildSchemaId(properties));
  }

  /**
   * Incassant id deel opbouwen.
   * 
   * @return
   */
  private PartyIdentification32 buildSchemaId(IncassoProperties properties) {
    PartyIdentification32 schemaId = new PartyIdentification32();
    schemaId.setNm(properties.getNaam());

    Party6Choice id = new Party6Choice();
    PersonIdentification5 personId = new PersonIdentification5();
    id.setPrvtId(personId);
    GenericPersonIdentification1 genId = new GenericPersonIdentification1();
    personId.getOthr().add(genId);
    genId.setId(properties.getIncassantId());
    PersonIdentificationSchemeName1Choice schemaName = new PersonIdentificationSchemeName1Choice();
    schemaName.setPrtry("SEPA");
    genId.setSchmeNm(schemaName);

    schemaId.setId(id);
    return schemaId;
  }

}
