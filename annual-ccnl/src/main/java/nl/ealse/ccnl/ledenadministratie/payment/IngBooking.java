package nl.ealse.ccnl.ledenadministratie.payment;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
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

  private static final Map<String, XPathExpression> EXPRESSIONS = new HashMap<>();

  private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DF_KEY = DateTimeFormatter.ofPattern("yyyyMMdd");

  private static final TransformerFactory TF = TransformerFactory.newInstance();

  static {
    TF.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
    TF.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");

    XPath xpath = XPathUtil.newIngXPath();
    try {
      EXPRESSIONS.put("bedrag", xpath.compile("ing:Amt"));
      EXPRESSIONS.put("debet", xpath.compile("ing:CdtDbtInd"));
      EXPRESSIONS.put("boekdatum", xpath.compile("ing:BookgDt/ing:Dt"));
      EXPRESSIONS.put("d.naam",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Cdtr/ing:Nm"));
      EXPRESSIONS.put("c.naam",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Dbtr/ing:Nm"));
      EXPRESSIONS.put("d.rekening",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:CdtrAcct/ing:Id/ing:IBAN"));
      EXPRESSIONS.put("c.rekening",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:DbtrAcct/ing:Id/ing:IBAN"));
      EXPRESSIONS.put("d.adres",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Cdtr/ing:PstlAdr/ing:AdrLine"));
      EXPRESSIONS.put("c.adres",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RltdPties/ing:Dbtr/ing:PstlAdr/ing:AdrLine"));
      EXPRESSIONS.put("omschrijving",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RmtInf/ing:Ustrd"));
      EXPRESSIONS.put("stornoInfo",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:Refs/ing:EndToEndId"));
      EXPRESSIONS.put("cancelReason",
          xpath.compile("ing:NtryDtls/ing:TxDtls/ing:RtrInf/ing:Rsn/ing:Cd"));
      EXPRESSIONS.put("bookingType", xpath.compile("ing:BkTxCd/ing:Domn/ing:Fmly/ing:Cd"));
    } catch (XPathExpressionException e) {
      log.error("", e);
      throw new ExceptionInInitializerError(e);
    }
  }

  private final Element element;

  private boolean contributie = true;

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

  private int key;

  public IngBooking(Element element) {
    this.element = element;
  }

  public Double getBedrag() {
    if (bedrag == null) {
      bedrag = Double.parseDouble(getValue("bedrag"));
      bedrag = isDebet() ? bedrag * -1 : bedrag;
    }
    return bedrag;
  }

  public boolean isDebet() {
    if (debet == null) {
      debet = "DBIT".equals(getValue("debet"));
    }
    return debet;
  }

  public LocalDate getBoekdatum() {
    if (boekdatum == null) {
      try {
        String datum = getValue("boekdatum");
        boekdatum = LocalDate.parse(datum, DF);
      } catch (DateTimeParseException e) {
        throw new PaymentException(e);
      }
    }
    return boekdatum;
  }

  public String getNaam() {
    if (naam == null || naam.length() == 0) {
      if (isDebet()) {
        naam = getValue("d.naam");
      } else {
        naam = getValue("c.naam");
      }
    }
    return naam;
  }

  public String getAdres() {
    if (adres == null) {
      final NodeList result;
      if (isDebet()) {
        result = getNodeList("d.adres");
      } else {
        result = getNodeList("c.adres");
      }
      if (result.getLength() > 1) {
        adres = result.item(0).getTextContent();
      } else {
        adres = "";
      }
    }
    return adres;
  }

  public String getPostcode() {
    if (postcode == null) {
      final NodeList result;
      if (isDebet()) {
        result = getNodeList("d.adres");
      } else {
        result = getNodeList("c.adres");
      }
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
      if (isDebet()) {
        tegenRekening = getValue("d.rekening");
      } else {
        tegenRekening = getValue("c.rekening");
      }
    }
    return tegenRekening;
  }

  public String getOmschrijving() {
    if (omschrijving == null) {
      omschrijving = getValue("omschrijving");
    }
    return omschrijving;
  }

  public String getStorneringInfo() {
    if (stornoInfo == null) {
      stornoInfo = getValue("stornoInfo");
    }
    return stornoInfo;
  }

  public BookingType getTypebooking() {
    if (bookingType == null) {
      String type = getValue("bookingType");
      bookingType = BookingType.valueOf(type);
    }
    return bookingType;
  }

  public CancelReason getStornoReden() {
    if (cancelReason == null) {
      String reden = getValue("cancelReason");
      cancelReason = CancelReason.valueOf(reden);
    }
    return cancelReason;
  }

  private String getValue(String key) {
    try {
      return EXPRESSIONS.get(key).evaluate(element);
    } catch (XPathExpressionException e) {
      throw new PaymentException(e);
    }
  }

  private NodeList getNodeList(String key) {
    try {
      return (NodeList) EXPRESSIONS.get(key).evaluate(element, XPathConstants.NODESET);
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

}
