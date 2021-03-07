package nl.ealse.ccnl.ledenadministratie.dd;

import java.time.LocalDate;
import nl.ealse.ccnl.ledenadministratie.dd.model.AccountIdentification4Choice;
import nl.ealse.ccnl.ledenadministratie.dd.model.ActiveOrHistoricCurrencyAndAmount;
import nl.ealse.ccnl.ledenadministratie.dd.model.BranchAndFinancialInstitutionIdentification4;
import nl.ealse.ccnl.ledenadministratie.dd.model.CashAccount16;
import nl.ealse.ccnl.ledenadministratie.dd.model.ChargeBearerType1Code;
import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransaction6;
import nl.ealse.ccnl.ledenadministratie.dd.model.DirectDebitTransactionInformation9;
import nl.ealse.ccnl.ledenadministratie.dd.model.FinancialInstitutionIdentification7;
import nl.ealse.ccnl.ledenadministratie.dd.model.MandateRelatedInformation6;
import nl.ealse.ccnl.ledenadministratie.dd.model.PartyIdentification32;
import nl.ealse.ccnl.ledenadministratie.dd.model.PaymentIdentification1;
import nl.ealse.ccnl.ledenadministratie.dd.model.RemittanceInformation5;
import nl.ealse.ccnl.ledenadministratie.excel.dd.BicResolver;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

/**
 * Debiteur informatie deel opbouwen.
 * 
 * @author Ealse
 *
 */
public class DirectDebitTransactionInformationBuilder {

  /**
   * Utility om te checken of het IBAN-nummer geldig is.
   */
  private static final IBANCheckDigit IBAN_CHECK = new IBANCheckDigit();

  private static final LocalDate START_MANDATE = LocalDate.of(2009, 11, 01);

  private final IncassoProperties properties;

  /**
   * Het op te bouwen object.
   */
  private DirectDebitTransactionInformation9 transactie = new DirectDebitTransactionInformation9();

  public DirectDebitTransactionInformationBuilder(IncassoProperties properties) {
    this.properties = properties;
    init(properties);
  }

  /**
   * Naam van de debiteur toevoegen.
   * 
   * @param naam - debiteurnaam
   * @return builder
   */
  public DirectDebitTransactionInformationBuilder metDibiteurNaam(String naam) {
    PartyIdentification32 debiteur = new PartyIdentification32();
    debiteur.setNm(naam);
    transactie.setDbtr(debiteur);
    return this;
  }

  /**
   * IBAN-nummer van de debiteur toevoegen. DE BIC-code wordt erbij gezocht en toegevoegd.
   * 
   * @param iban - toe te voegen IBAN-nummer
   * @return builder
   * @throws InvalidIbanException
   */
  public DirectDebitTransactionInformationBuilder metDibiteurIBAN(String iban)
      throws InvalidIbanException {
    if (!IBAN_CHECK.isValid(iban)) {
      throw new InvalidIbanException(String.format("IBAN is ongeldig '%s'", iban));
    }
    CashAccount16 ibanRekening = new CashAccount16();
    AccountIdentification4Choice ibanNummer = new AccountIdentification4Choice();
    ibanNummer.setIBAN(iban);
    ibanRekening.setId(ibanNummer);
    transactie.setDbtrAcct(ibanRekening);
    BranchAndFinancialInstitutionIdentification4 bic =
        new BranchAndFinancialInstitutionIdentification4();
    FinancialInstitutionIdentification7 finId = new FinancialInstitutionIdentification7();
    bic.setFinInstnId(finId);
    finId.setBIC(BicResolver.getBicCode(iban));
    transactie.setDbtrAgt(bic);
    return this;
  }

  /**
   * Incasso omschrijving toevoegen.
   * 
   * @param lidnummer - toe te voegen nummer CCNL-lid
   * @return builder
   */
  public DirectDebitTransactionInformationBuilder metLidnummer(Integer lidnummer) {
    PaymentIdentification1 reden = new PaymentIdentification1();
    reden.setEndToEndId("lid " + lidnummer.toString());
    transactie.setPmtId(reden);
    RemittanceInformation5 referentie = new RemittanceInformation5();
    referentie.getUstrd().add(properties.getIncassoReden());
    transactie.setRmtInf(referentie);
    transactie.setDrctDbtTx(getMandaat(lidnummer));
    return this;
  }


  /**
   * Mandaat gegevens invoegen voor IBAN-mandaat
   * 
   * @param lidnummer - nummer waarvoor mandaat wordt toegevoegd
   * @return builder
   */
  private DirectDebitTransaction6 getMandaat(Integer lidnummer) {
    DirectDebitTransaction6 ddtx = new DirectDebitTransaction6();
    MandateRelatedInformation6 mandaat = new MandateRelatedInformation6();
    mandaat.setMndtId(String.format(properties.getMachtigingReferentie(), lidnummer));
    mandaat.setDtOfSgntr(DateUtil.toXMLDate(START_MANDATE));
    ddtx.setMndtRltdInf(mandaat);
    return ddtx;

  }

  /**
   * Object object opvragen.
   * @return gebouwde object
   */
  public DirectDebitTransactionInformation9 build() {
    return transactie;
  }

  /**
   * Initialisatie van vaste gegevens.
   */
  private void init(IncassoProperties properties) {
    ActiveOrHistoricCurrencyAndAmount bedraginfo = new ActiveOrHistoricCurrencyAndAmount();
    bedraginfo.setCcy("EUR");
    bedraginfo.setValue(properties.getIncassoBedrag());
    transactie.setInstdAmt(bedraginfo);
    RemittanceInformation5 referentie = new RemittanceInformation5();
    referentie.getUstrd().add(properties.getIncassoReden());
    transactie.setRmtInf(referentie);
    transactie.setChrgBr(ChargeBearerType1Code.SLEV);
  }

}
