package nl.ealse.ccnl.ledenadministratie.dd;

import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.ObjectFactory;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentRepository;
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
@Transactional
public class SepaIncassoGenerator {

  private final IncassoProperties incassoProperties;
  private final CCNLColumnProperties excelProperties;
  private final MemberRepository dao;
  private final DocumentRepository documentDao;

  public SepaIncassoGenerator(CCNLColumnProperties excelProperties,
      IncassoProperties incassoProperties, MemberRepository dao, DocumentRepository documentDao) {
    this.incassoProperties = incassoProperties;
    this.excelProperties = excelProperties;
    this.dao = dao;
    this.documentDao = documentDao;
  }

  /**
   * SEPA INCASSO bestand aanmaken op basis van het ledenbestand
   * 
   * @param targetFile - doel incassobestand
   * @param controlExcelFile - Excel controlebestand
   * @return aangemaakte SEPA INCASSO document
   * @throws IncassoException - fout tijdens aanmaken incassodocument
   */
  public SepaIncassoResult generateSepaDirectDebitFile(File targetFile, File controlExcelFile)
      throws IncassoException {
    incassoProperties.load();
    SepaIncassoContext context = new SepaIncassoContext(incassoProperties, excelProperties);
    SepaIncassoDocumentGenerator documentGenerator =
        new SepaIncassoDocumentGenerator(context);
    List<Member> members = dao.findMemberByPaymentMethodAndMemberStatusAndCurrentYearPaidOrderByMemberNumber(
        PaymentMethod.DIRECT_DEBIT, MembershipStatus.ACTIVE, false);
    List<Integer> sepaNumbers = documentDao.findMemberNummbersWithSepa();
    Document incassoDocument = documentGenerator.generateIncassoDocument(controlExcelFile, members, sepaNumbers);
    maakSepaIncassoBestand(incassoDocument, targetFile);
    return new SepaIncassoResult(documentGenerator.getAantalTransacties(), context.getMessages());
  }

  /**
   * Zet het SEPA INCASSO document om naar een XML-bestand.
   * 
   * @param document - het weg te schrijven incassodocument
   * @param targetFile - het doelbestand
   * @throws IncassoException - fout tijdens aanmaken incassobestand
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
