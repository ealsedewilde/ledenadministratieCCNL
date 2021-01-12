package nl.ealse.ccnl.ledenadministratie.payment;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class XPathUtil {

  public XPath newIngXPath() {
    XPath xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(new NamespaceContext() {

      @Override
      public Iterator<String> getPrefixes(String arg0) {
        return null;
      }

      @Override
      public String getPrefix(String arg0) {
        return null;
      }

      @Override
      public String getNamespaceURI(String prefix) {
        return "urn:iso:std:iso:20022:tech:xsd:camt.053.001.02";
      }

    });
    return xpath;
  }


}
