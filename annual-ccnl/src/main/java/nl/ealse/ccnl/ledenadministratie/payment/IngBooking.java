package nl.ealse.ccnl.ledenadministratie.payment;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Slf4j
public class IngBooking implements Comparable<IngBooking> {

  private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DF_KEY = DateTimeFormatter.ofPattern("yyyyMMdd");

  private static final TransformerFactory TF = TransformerFactory.newInstance();
  private static final XPathConfig xpathConfig = new XPathConfig();

  static {
    TF.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "");
    TF.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
  }

  /**
   * The ban transaction that is wrapper by this class.
   */
  private final Element element;

  /**
   * Expression used to extract data from the <code>element</code>.
   */
  private Map<Token, XPathExpression> expressions;

  /**
   * This booking is a membership fee.
   * This is determined during the reconciliation process.
   */
  private boolean contributie = true;

  /**
   * Member number,
   * This number is determined during the reconciliation process.
   */
  private int lidnummer;

  private Double bedrag;
  private Boolean debet;
  private String naam;
  private String postcode;
  private String adres;
  private String tegenRekening;
  private String omschrijving;
  private String stornoInfo;
  private BookingType bookingType;
  private CancelReason cancelReason;

  private LocalDate boekdatum;

  /**
   * Unique key for this booking
   * It consists of the booking dat (yyMMdd + member number.
   */
  private int key;

  public IngBooking(Element element) {
    this.element = element;
    this.expressions = xpathConfig.getCreditExpressions();
    if (isDebet()) {
      this.expressions = xpathConfig.getDebitExpressions();
    }
  }

  private boolean isDebet() {
    if (debet == null) {
      debet = "DBIT".equals(getValue(Token.DEBET));
    }
    return debet;
  }

  public Double getBedrag() {
    if (bedrag == null) {
      bedrag = Double.parseDouble(getValue(Token.BEDRAG));
      bedrag = isDebet() ? bedrag * -1 : bedrag;
    }
    return bedrag;
  }

  public LocalDate getBoekdatum() {
    if (boekdatum == null) {
      try {
        String datum = getValue(Token.BOEKDATUM);
        boekdatum = LocalDate.parse(datum, DF);
      } catch (DateTimeParseException e) {
        throw new PaymentException(e);
      }
    }
    return boekdatum;
  }

  public String getNaam() {
    if (naam == null || naam.length() == 0) {
      naam = getValue(Token.NAAM);
    }
    return naam;
  }

  public String getAdres() {
    if (adres == null) {
      final NodeList result = getNodeList(Token.ADRES);
      if (result.getLength() > 0) {
        adres = result.item(0).getTextContent();
      } else {
        adres = "";
      }
    }
    return adres;
  }

  public String getPostcode() {
    if (postcode == null) {
      final NodeList result = getNodeList(Token.ADRES);
      if (result.getLength() > 1) {
        postcode = result.item(1).getTextContent().replaceAll("\\s", "").substring(0, 6);
      } else {
        postcode = "";
      }
    }
    return postcode;
  }

  public String getTegenRekening() {
    if (tegenRekening == null) {
      tegenRekening = getValue(Token.TEGEN_REKENING);
    }
    return tegenRekening;
  }

  public String getOmschrijving() {
    if (omschrijving == null) {
      omschrijving = getValue(Token.OMSCHRIJVING);
    }
    return omschrijving;
  }

  public String getStorneringInfo() {
    if (stornoInfo == null) {
      stornoInfo = getValue(Token.STORNO_INFO);
    }
    return stornoInfo;
  }

  public BookingType getTypebooking() {
    if (bookingType == null) {
      String type = getValue(Token.BOOKING_TYPE);
      bookingType = BookingType.valueOf(type);
    }
    return bookingType;
  }

  public CancelReason getStornoReden() {
    if (cancelReason == null) {
      String reden = getValue(Token.CANCEL_REASON);
      cancelReason = CancelReason.valueOf(reden);
    }
    return cancelReason;
  }

  private String getValue(Token key) {
    try {
      XPathExpression exp = expressions.get(key);
      return exp.evaluate(element);
    } catch (XPathExpressionException e) {
      throw new PaymentException(e);
    }
  }

  private NodeList getNodeList(Token key) {
    try {
      return (NodeList) expressions.get(key).evaluate(element, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      throw new PaymentException(e);
    }
  }

  public boolean isStornering() {
    return BookingType.IDDT.equals(getTypebooking());
  }

  public boolean isContributie() {
    return contributie;
  }

  public void setContributie(boolean contributie) {
    this.contributie = contributie;
  }


  public int getLidnummer() {
    return lidnummer;
  }

  public void setLidnummer(int lidnummer) {
    this.lidnummer = lidnummer;
  }

  public String toString() {
    try {
      Transformer transformer = TF.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(element);
      transformer.transform(source, result);
      return result.getWriter().toString();
    } catch (Exception e) {
      log.error("Unable to generate String representation of this object", e);
      return "Unable to generate String representation of this object";
    }
  }

  private int getKey() {
    if (key == 0) {
      String h = getBoekdatum().format(DF_KEY);
      key = Integer.parseInt(h) * 1000 + lidnummer;
    }
    return key;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof IngBooking) {
      IngBooking b = (IngBooking) o;
      return this.getKey() == b.getKey();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getKey();
  }

  @Override
  public int compareTo(IngBooking o) {
    return this.getKey() - o.getKey();
  }

  private enum Token {
    BOOKING_TYPE, BEDRAG, BOEKDATUM, DEBET, NAAM, POSTCODE, ADRES, TEGEN_REKENING, OMSCHRIJVING, STORNO_INFO, CANCEL_REASON
  }
  
  private static final class XPathConfig {
    private final Map<Token, XPathExpression> debitExpressions = new EnumMap<>(Token.class);
    private final Map<Token, XPathExpression> creditExpressions = new EnumMap<>(Token.class);
    
    private XPathConfig() {
      init();
    }

    public void init() {
      XPath xpath = XPathUtil.newIngXPath();

      try {
        debitExpressions.put(Token.DEBET, xpath.compile("ing:CdtDbtInd"));
        debitExpressions.put(Token.BEDRAG, xpath.compile("ing:Amt"));
        debitExpressions.put(Token.BOEKDATUM, xpath.compile("ing:BookgDt/ing:Dt"));
        debitExpressions.put(Token.OMSCHRIJVING,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RmtInf/ing:Ustrd"));
        debitExpressions.put(Token.STORNO_INFO,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:Refs/ing:EndToEndId"));
        debitExpressions.put(Token.CANCEL_REASON,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RtrInf/ing:Rsn/ing:Cd"));
        debitExpressions.put(Token.BOOKING_TYPE,
            xpath.compile("ing:BkTxCd/ing:Domn/ing:Fmly/ing:Cd"));

        creditExpressions.putAll(debitExpressions);

        debitExpressions.put(Token.NAAM,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Cdtr/ing:Nm"));
        creditExpressions.put(Token.NAAM,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Dbtr/ing:Nm"));
        debitExpressions.put(Token.TEGEN_REKENING,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:CdtrAcct/ing:Id/ing:IBAN"));
        creditExpressions.put(Token.TEGEN_REKENING,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:DbtrAcct/ing:Id/ing:IBAN"));
        debitExpressions.put(Token.ADRES,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Cdtr/ing:PstlAdr/ing:AdrLine"));
        creditExpressions.put(Token.ADRES,
            xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Dbtr/ing:PstlAdr/ing:AdrLine"));

      } catch (XPathExpressionException e) {
        log.error("Invalid XPath", e);
        throw new ExceptionInInitializerError(e);
      }
    }
    
    public Map<Token, XPathExpression> getDebitExpressions() {
      return debitExpressions;
    }
    
    public Map<Token, XPathExpression> getCreditExpressions() {
      return creditExpressions;
    }
    
  }
  
  

}
