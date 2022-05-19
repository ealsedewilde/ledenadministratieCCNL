package nl.ealse.ccnl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.DocumentTemplateRepository;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
  
  @Mock
  private DocumentRepository dao;
  
  @Mock
  private DocumentTemplateRepository templateDao;
  
  @InjectMocks
  private DocumentService sut;
  
  @Test
  void findDocumentsTest() {
    Member owner = new Member();
    sut.findDocuments(owner);
    verify(dao).findByOwnerOrderByCreationDateDesc(owner);
  }
  
  @Test
  void findDocumentsByTypeTest() {
    Member owner = new Member();
    sut.findDocuments(owner, DocumentType.OTHER);
    verify(dao).findByOwnerAndDocumentType(owner, DocumentType.OTHER);
  }
  
  @Test
  void saveDocumentTest() {
    Document document = new Document();
    sut.saveDocument(document);
    verify(dao).save(document);
  }
  
  @Test
  void deleteDocumentTest() {
    Document document = new Document();
    sut.deleteDocument(document);
    verify(dao).delete(document);
  }
  
  @Test
  void findSepaAuthorizationFormTest() {
    List<Document> documents = new ArrayList<>();
    when(dao.findByDocumentType(DocumentType.SEPA_AUTHORIZATION_FORM)).thenReturn(documents);
    Optional<Document> result = sut.findSepaAuthorizationForm();
    Assertions.assertFalse(result.isPresent());
  }
  
  @Test
  void findSepaAuthorizationTest() {
    List<Document> documents = new ArrayList<>();
    Member owner = new Member();
    when(dao.findByOwnerAndDocumentType(owner, DocumentType.SEPA_AUTHORIZATION)).thenReturn(documents);
    Optional<Document> result = sut.findSepaAuthorization(owner);
    Assertions.assertFalse(result.isPresent());
  }
  
  @Test
  void saveSepaAuthorizationFormTest() {
    List<Document> documents = new ArrayList<>();
    when(dao.findByDocumentType(DocumentType.SEPA_AUTHORIZATION_FORM)).thenReturn(documents);
    String name = "sepa.pdf";
    byte[] pdf = new byte[32];
    sut.saveSepaAuthorizationForm(name, pdf);
    verify(dao).save(any(Document.class));
  }
  
  @Test
  void findDocumentTemplateTest() {
    DocumentTemplateID id = new DocumentTemplateID();
    List<DocumentTemplate> templates = new ArrayList<>();
    when(templateDao.findByTemplateID(id)).thenReturn(templates);
    Optional<DocumentTemplate> result = sut.findDocumentTemplate(id);
    Assertions.assertFalse(result.isPresent());
  }
  
  @Test
  void generateFOTest() {
    LetterData data = new LetterData("content");
    FOContent content = sut.generateFO(data);
    Assertions.assertEquals(3, content.getContentSnippets().size());
  }

}
