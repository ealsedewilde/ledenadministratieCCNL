package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExternalRelationClubRepository
    extends ExternalRelationRepository<ExternalRelationClub> {

  @Query("Select m.relationNumber from ExternalRelationClub m")
  List<Number> getAllRelationNumbers();


}
