package nl.ealse.ccnl.service;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentTemplateRepository;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.FOGenerator;
import nl.ealse.ccnl.ledenadministratie.pdf.PDFGenerator;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.ledenadministratie.word.LetterGenerator;

@Slf4j
public class DocumentService {
  
  @Getter
  private static DocumentService instance = new DocumentService();

  private final DocumentRepository dao;

  private final DocumentTemplateRepository templateDao;

  private DocumentService() {
    log.info("Service created");
    this.dao = DocumentRepository.getInstance();
    this.templateDao = DocumentTemplateRepository.getInstance();
  }

  public List<Document> findDocuments(Member owner) {
    return dao.findByOwnerOrderByCreationDateDesc(owner);
  }

  public List<Document> findDocuments(Member owner, DocumentType type) {
    return dao.findByOwnerAndDocumentType(owner, type);
  }

  public void saveDocument(Document document) {
    dao.save(document);
  }

  public void deleteDocument(Document document) {
    dao.delete(document);
  }

  public Optional<Document> findSepaAuthorizationForm() {
    List<Document> documents = dao.findByDocumentType(DocumentType.SEPA_AUTHORIZATION_FORM);
    if (documents.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(documents.get(0));
    }
  }

  public void saveSepaAuthorizationForm(String name, byte[] content) {
    List<Document> documents = dao.findByDocumentType(DocumentType.SEPA_AUTHORIZATION_FORM);
    Document form;
    if (documents.isEmpty()) {
      form = new Document();
      form.setDocumentType(DocumentType.SEPA_AUTHORIZATION_FORM);
      form.setDocumentName(name);
    } else {
      form = documents.get(0);
    }
    form.setPdf(content);
    dao.save(form);
  }

  public Optional<Document> findSepaAuthorization(Member owner) {
    List<Document> documents =
        dao.findByOwnerAndDocumentType(owner, DocumentType.SEPA_AUTHORIZATION);
    if (!documents.isEmpty()) {
      return Optional.of(documents.get(0));
    }
    return Optional.empty();
  }

  public List<DocumentTemplate> findAllDocumentTemplates() {
    return templateDao.findAllOrderByModificationDateDesc();
  }

  public List<DocumentTemplate> findDocumentTemplates(DocumentTemplateType type) {
    return templateDao.findByDocumentTemplateTypeOrderByModificationDateDesc(type);
  }

  public List<DocumentTemplate> findDocumentTemplates(DocumentTemplateType type, boolean withSepa) {
    return templateDao.findByDocumentTemplateTypeOrderByModificationDateDesc(type, withSepa);
  }

  public Optional<DocumentTemplate> findDocumentTemplate(DocumentTemplateID id) {
    return templateDao.findById(id);
  }

  public void persistDocumentemplate(DocumentTemplate template) {
    templateDao.save(template);
  }

  public void deleteDocumentTemplate(DocumentTemplate template) {
    templateDao.delete(template);
  }

  public byte[] generateWordDocument(LetterData data) {
    LetterGenerator letterGenerator = new LetterGenerator();
    return letterGenerator.generateDocument(data);
  }

  public byte[] generatePDF(LetterData data) {
    return PDFGenerator.generatePDF(FOGenerator.generateFO(data), data);
  }

  public byte[] generatePDF(FOContent foContent, LetterData data) {
    return PDFGenerator.generatePDF(foContent, data);
  }

  public byte[] generatePDF(String fo) {
    return PDFGenerator.generatePDF(fo);
  }

  public FOContent generateFO(LetterData data) {
    return FOGenerator.generateFO(data);
  }

}
