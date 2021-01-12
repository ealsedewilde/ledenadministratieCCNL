package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationOther;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExternalRelationOtherRepository
    extends ExternalRelationRepository<ExternalRelationOther> {

  @Query("Select m.relationNumber from ExternalRelationOther m")
  List<Number> getAllRelationNumbers();


}
