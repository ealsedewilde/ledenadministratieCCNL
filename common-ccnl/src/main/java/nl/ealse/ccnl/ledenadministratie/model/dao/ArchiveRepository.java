package nl.ealse.ccnl.ledenadministratie.model.dao;

import jakarta.persistence.Query;
import java.util.List;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;

public class ArchiveRepository extends BaseRepository<ArchivedMember> {

  @Getter
  private static ArchiveRepository instance = new ArchiveRepository();

  private ArchiveRepository() {
    super(ArchivedMember.class);
  }

  public void deleteObseleteArchivedMembers(Integer year) {
    Query query =
        getEntityManager().createQuery("DELETE FROM ArchivedMember A WHERE A.id.archiveYear <= ?1");
    query.setParameter(1, year);
    query.executeUpdate();
  }

  public List<ArchivedMember> findAllByYearAndMemberNumber() {
    return executeQuery(
        "SELECT A FROM ArchivedMember A ORDER BY A.id.archiveYear DESC, A.id.memberNumber ASC");
  }

  @Override
  protected Object getPrimaryKey(ArchivedMember entity) {
    return entity.getId();
  }

}
