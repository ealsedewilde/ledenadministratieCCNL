package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplate;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateID;
import nl.ealse.ccnl.ledenadministratie.model.DocumentTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, String> {

  @Query("select t from DocumentTemplate t order by t.modificationDate desc")
  List<DocumentTemplate> findAllOrderByModificationDateDesc();

  @Query("select t from DocumentTemplate t where t.templateID.documentTemplateType = ?1"
      + " order by t.modificationDate desc")
  List<DocumentTemplate> findByDocumentTemplateTypeOrderByModificationDateDesc(
      DocumentTemplateType type);

  @Query("select t from DocumentTemplate t where t.templateID.documentTemplateType = ?1 and t.includeSepaForm = ?2"
      + " order by t.modificationDate desc")
  List<DocumentTemplate> findByDocumentTemplateTypeOrderByModificationDateDesc(
      DocumentTemplateType type, boolean includeSepaForm);

  List<DocumentTemplate> findByTemplateID(DocumentTemplateID id);

}
