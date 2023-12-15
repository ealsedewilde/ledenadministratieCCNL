package nl.ealse.ccnl.ledenadministratie.dd;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dd.model.Document;
import nl.ealse.ccnl.ledenadministratie.dd.model.ObjectFactory;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;


/**
 * SEPA INCASSO bestand aanmaken.
 *
 * @author Ealse
 *
 */
@Slf4j
public class SepaIncassoGenerator {
  
  @Getter
  private static SepaIncassoGenerator instance = new SepaIncassoGenerator();

  private final MemberRepository dao;
  private final DocumentRepository documentDao;

  private SepaIncassoGenerator() {
    this.dao = MemberRepository.getInstance();
    this.documentDao = DocumentRepository.getInstance();
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
    SepaIncassoContext context = new SepaIncassoContext();
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
