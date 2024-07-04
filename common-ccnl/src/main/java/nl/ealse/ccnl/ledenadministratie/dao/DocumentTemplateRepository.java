package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;

public class DocumentTemplateRepository extends BaseRepository<DocumentTemplate> {

  public DocumentTemplateRepository() {
    super(DocumentTemplate.class);
  }

  @Override
  protected Object getPrimaryKey(DocumentTemplate entity) {
    return entity.getTemplateID();
  }

  public List<DocumentTemplate> findAllOrderByModificationDateDesc() {
    return executeQuery("select t from DocumentTemplate t order by t.modificationDate desc");
  }

  public List<DocumentTemplate> findByDocumentTemplateTypeOrderByModificationDateDesc(
      DocumentTemplateType type) {
    return executeQuery("select t from DocumentTemplate t where"
        + " t.templateID.documentTemplateType = ?1 order by t.modificationDate desc", type);
  }

  public List<DocumentTemplate> findByDocumentTemplateTypeOrderByModificationDateDesc(
      DocumentTemplateType type, boolean includeSepaForm) {
    return executeQuery(
        "select t from DocumentTemplate t where t.templateID.documentTemplateType = ?1"
            + " and t.includeSepaForm = ?2 order by t.modificationDate desc",
        type, includeSepaForm);
  }

  public List<DocumentTemplate> findByTemplateID(DocumentTemplateID id) {
    return executeQuery(
        "select t from DocumentTemplate t where t.templateID.documentTemplateType = ?1"
        + " and t.templateID.name = ?2",
        id.getDocumentTemplateType(), id.getName());

  }

}
