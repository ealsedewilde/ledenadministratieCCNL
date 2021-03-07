package nl.ealse.ccnl.pdf;

import static org.apache.xmlgraphics.util.MimeConstants.MIME_PDF;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import nl.ealse.ccnl.ledenadministratie.pdf.FopFactoryProvider;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class MailPDFTest {

  private static final TransformerFactory factory = TransformerFactory.newInstance();

  private static final FopFactory fopFactory;

  static {
    fopFactory = FopFactoryProvider.getFopFactory();
  }

  private final Resource template = new ClassPathResource("test.fo");

  @Test
  void testMail() {
    try (InputStream is = template.getInputStream()) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Fop fop = fopFactory.newFop(MIME_PDF, out);

      Source src = new StreamSource(is);
      Result res = new SAXResult(fop.getDefaultHandler());

      Transformer transformer = factory.newTransformer();
      transformer.transform(src, res);
      byte[] pdf = out.toByteArray();
      out.close();
      Assertions.assertEquals(5912, pdf.length);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
