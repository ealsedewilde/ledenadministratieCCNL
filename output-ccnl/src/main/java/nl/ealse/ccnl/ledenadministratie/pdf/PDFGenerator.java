package nl.ealse.ccnl.ledenadministratie.pdf;

import static org.apache.xmlgraphics.util.MimeConstants.MIME_PDF;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.GeneratorException;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.xml.sax.SAXException;

public class PDFGenerator {

  private static final TransformerFactory factory = TransformerFactory.newInstance();

  private final FopFactory fopFactory;

  public PDFGenerator() {
    this.fopFactory = FopFactoryProvider.getFopFactory();
  }

  public byte[] generatePDF(FOContent content, LetterData data) {
    StringBuilder sb = new StringBuilder();
    sb.append(content.getPreContent());
    for (Member member : data.getMembers()) {
      sb.append(content.getContent(member));
    }
    sb.append(content.getPostContent());
    return generatePDF(sb.toString());
  }

  public byte[] generatePDF(String fo) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Fop fop = fopFactory.newFop(MIME_PDF, out);

      Source src = new StreamSource(new StringReader(fo));
      Result res = new SAXResult(fop.getDefaultHandler());

      Transformer transformer = factory.newTransformer();
      transformer.transform(src, res);
      out.close();
      return out.toByteArray();
    } catch (IOException | SAXException | TransformerException e) {
      throw new GeneratorException("Error generating PDF", e);
    }

  }

  public byte[] generateMailPDF(FOContent content) {
    return generatePDF(content.toString());
  }


}
