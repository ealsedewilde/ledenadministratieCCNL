package nl.ealse.ccnl.ledenadministratie.payment;

public enum CancelReason {

  AC01("Rekeningnummer onjuist"), AC04("Rekening opgeheven"), AC06("Rekeningnummer geblokkeerd"),

  /*
   * U kunt van deze rekening niet incasseren, omdat het bijvoorbeeld een spaarrekening betreft
   */
  AG01("Rekeningnummer niet toegestaan"),

  /*
   * Het INCASSO is aangeleverd met een onjuiste volgorde type. • U heeft volgorde type ‘Recurrent’
   * (doorlopend) gebruikt terwijl u nog geen ‘First’ (eerste) heeft aangeleverd. • Of u heeft het
   * volgorde type ‘First’ (eerste) gebruikt terwijl u al eerder een INCASSO met volgorde type
   * ‘First’ of ‘Recurrent’ (doorlopend) heeft aangeleverd. • Of u heeft het volgorde type ‘First’
   * (eerste) of ‘Recurrent’ (doorlopend) gebruikt terwijl u al eerder een INCASSO met volgorde type
   * ‘one off’ (eenmalig) heeft gebruikt. • Of u heeft het volgorde type ‘One off’ (eenmalig)
   * gebruikt terwijl u onder hetzelfde machtigingskenmerk al eerder een ‘One off’ INCASSO heeft
   * aangeleverd.
   */
  AG02("Ongeldige bank transactie code"), AM04("Onvoldoende saldo"), MD01(
      "Geen machtiging verstrekt"), MD02("Machtigingsgegevens niet volledig of niet juist"),
  /*
   *
   */
  MD06("Terugboeking op verzoek klant"),

  /*
   * De debiteur heeft het INCASSO vóór de verwerkingsdatum geweigerd.
   */
  MS02("Onbekende reden klant"),

  /*
   * Het INCASSO is zonder specifieke reden geweigerd. Er kan sprake zijn van onvoldoende saldo
   * (AM04) op de rekening van de debiteur. Als gevolg van privacywetgeving kan het voorkomen dat
   * banken de code AM04 niet doorgegeven maar verantwoorden als MS03.
   */
  MS03("Onbekende reden bank"),

  /*
   * Het INCASSO is geweigerd omdat: • het maximum ingestelde incassobedrag is overschreden • de
   * debiteur een incassoblokkade op rekening heeft geplaatst.
   */
  SL01("Specifieke dienstverlening bank");

  private final String reden;

  private CancelReason(String reden) {
    this.reden = reden;
  }

  public String getReden() {
    return reden;
  }

  @Override
  public String toString() {
    return name() + ": " + reden;
  }

}
