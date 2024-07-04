package nl.ealse.ccnl.ledenadministratie.dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public class DocumentRepository extends BaseRepository<Document> {
   
  public DocumentRepository() {
    super(Document.class);
  }

  @Override
  protected Object getPrimaryKey(Document entity) {
    return entity.getId();
  }

  public List<Document> findByDocumentType(DocumentType type) {
    return executeQuery("SELECT D FROM Document D WHERE D.documentType = ?1", type);
  }

  public List<Document> findByOwnerOrderByCreationDateDesc(Member owner) {
    return executeQuery("SELECT D FROM Document D WHERE D.owner = ?1 ORDER BY D,creationDate DESC", owner);
  }

  public List<Document> findByOwnerAndDocumentType(Member owner, DocumentType type) {
    return executeQuery("SELECT D FROM Document D WHERE D.owner = ?1 AND D.documentType = ?2", owner, type);
  }

  public List<Integer> findMemberNummbersWithSepa() {
    TypedQuery<Integer> query = getEntityManager().createQuery(
        "SELECT D.owner.memberNumber FROM Document D WHERE D.documentType = nl.ealse.ccnl.ledenadministratie.model.DocumentType.SEPA_AUTHORIZATION ORDER BY D.owner.memberNumber",
        Integer.class);
    return query.getResultList();
  }

}
