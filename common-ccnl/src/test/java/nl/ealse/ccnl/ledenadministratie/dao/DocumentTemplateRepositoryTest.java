package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTemplateRepositoryTest {

  private DocumentTemplateRepository dao = DocumentTemplateRepository.getInstance();

  @Test
  void doTest() {
    DocumentTemplate t = new DocumentTemplate();
    DocumentTemplateID id = new DocumentTemplateID();
    id.setDocumentTemplateType(DocumentTemplateType.MEMBERSHIP_CANCELATION_MAIL);
    id.setName("defaultmail");
    t.setTemplateID(id);
    t.setIncludeSepaForm(false);
    t.setTemplate("mail content");
    dao.save(t);

    t = new DocumentTemplate();
    id = new DocumentTemplateID();
    id.setDocumentTemplateType(DocumentTemplateType.PAYMENT_REMINDER);
    id.setName("defaultreminder");
    t.setTemplateID(id);
    t.setIncludeSepaForm(false);
    t.setTemplate("reminder content");
    dao.save(t);

    t = new DocumentTemplate();
    id = new DocumentTemplateID();
    id.setDocumentTemplateType(DocumentTemplateType.PAYMENT_REMINDER);
    id.setName("separeminder");
    t.setTemplateID(id);
    t.setIncludeSepaForm(true);
    t.setTemplate("sepa reminder content");
    dao.save(t);

    t = new DocumentTemplate();
    id = new DocumentTemplateID();
    id.setDocumentTemplateType(DocumentTemplateType.WELCOME_LETTER);
    id.setName("defaultwelcome");
    t.setTemplateID(id);
    t.setIncludeSepaForm(false);
    t.setTemplate("welcome content");
    dao.save(t);

    t = new DocumentTemplate();
    id = new DocumentTemplateID();
    id.setDocumentTemplateType(DocumentTemplateType.WELCOME_LETTER);
    id.setName("sepawelcome");
    t.setTemplateID(id);
    t.setIncludeSepaForm(true);
    t.setTemplate("sepa welcome content");
    dao.save(t);

    List<DocumentTemplate> templates = dao.findAllOrderByModificationDateDesc();
    Assertions.assertEquals(5, templates.size());

  }

}
