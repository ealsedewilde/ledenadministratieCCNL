package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ArchiveRepository extends JpaRepository<ArchivedMember, Integer> {

  @Transactional
  @Modifying
  @Query("DELETE FROM ArchivedMember A WHERE A.id.archiveYear <= ?1")
  void deleteObseleteArchivedMembers(Integer year);

  @Query("SELECT A FROM ArchivedMember A ORDER BY A.id.archiveYear DESC, A.id.memberNumber ASC")
  List<ArchivedMember> findAllByYearAndMemberNumber();

}
