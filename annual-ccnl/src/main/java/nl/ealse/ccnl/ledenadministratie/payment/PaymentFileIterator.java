package nl.ealse.ccnl.ledenadministratie.payment;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Slf4j
public class PaymentFileIterator implements Iterator<IngBooking> {

  private static final String XPATH_EXPRESSION =
      "/ing:Document/ing:BkToCstmrStmt/ing:Stmt/ing:Ntry[ing:Amt='27.50' or ing:Amt='30.00']";

  private int pointer = 0;

  private final NodeList nodes;

  public PaymentFileIterator(PaymentFile paymentFile) {
    nodes = init(paymentFile);
  }

  public NodeList init(PaymentFile paymentFile) {
    XPath xpath = XPathUtil.newIngXPath();
    try (Reader reader = new StringReader(paymentFile.getXml())) {
      InputSource inputSource = new InputSource(reader);
      NodeList nodeList =
          (NodeList) xpath.evaluate(XPATH_EXPRESSION, inputSource, XPathConstants.NODESET);
      log.info("Aantal transacties: " + nodeList.getLength());
      return nodeList;
    } catch (XPathExpressionException | IOException e) {
      log.error(String.format("Kan bestand %s niet verwerken", paymentFile.getFileName()));
      throw new PaymentException(e);
    }
  }

  @Override
  public boolean hasNext() {
    return pointer < nodes.getLength();
  }

  @Override
  public IngBooking next() {
    if (pointer >= nodes.getLength()) {
      throw new NoSuchElementException();
    }
    Element e = (Element) nodes.item(pointer++);
    return new IngBooking(e);
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();

  }

}
