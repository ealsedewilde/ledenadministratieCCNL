package nl.ealse.ccnl.ledenadministratie.dd;

import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.ObjectFactory;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.springframework.stereotype.Component;


/**
 * SEPA INCASSO bestand aanmaken.
 * 
 * @author Ealse
 *
 */
@Slf4j
@Component
public class SepaIncassoGenerator {

  private final IncassoProperties incassoProperties;
  private final CCNLColumnProperties excelProperties;
  private final MemberRepository dao;

  public SepaIncassoGenerator(CCNLColumnProperties excelProperties,
      IncassoProperties incassoProperties, MemberRepository dao) {
    this.incassoProperties = incassoProperties;
    this.excelProperties = excelProperties;
    this.dao = dao;
  }

  /**
   * SEPA INCASSO bestand aanmaken op basis van het ledenbestand
   * 
   * @return aangemaakte SEPA INCASSO document
   * @throws IncassoException
   */
  public void generateSepaDirectDebitFile(File targetFile, File controlExcelFile)
      throws IncassoException {
    SepaIncassoDocumentGenerator documentGenerator =
        new SepaIncassoDocumentGenerator(incassoProperties, excelProperties);
    List<Member> members = dao.findMemberByPaymentMethodAndMemberStatus(PaymentMethod.DIRECT_DEBIT,
        MembershipStatus.ACTIVE);
    Document incassoDocument = documentGenerator.generateIncassoDocument(controlExcelFile, members);
    maakSepaIncassoBestand(incassoDocument, targetFile);
  }

  /**
   * Zet het SEPA INCASSO document om naar een XML-bestand.
   * 
   * @param document
   * @throws IncassoException
   */
  private void maakSepaIncassoBestand(Document document, File targetFile) throws IncassoException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
      Marshaller marshaller = jaxbContext.createMarshaller();
      JAXBElement<Document> jaxbElement = (new ObjectFactory()).createDocument(document);
      marshaller.marshal(jaxbElement, targetFile);
      log.info("klaar: " + targetFile.getAbsolutePath());
    } catch (JAXBException e) {
      log.error("Fout bij aanmaken incassobestand XML", e);
      throw new IncassoException("Fout bij aanmaken incassobestand XML", e);
    }
  }

}
