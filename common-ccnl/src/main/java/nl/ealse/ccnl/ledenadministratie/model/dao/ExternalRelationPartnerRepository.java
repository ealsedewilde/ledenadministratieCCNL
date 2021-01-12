package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationPartner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExternalRelationPartnerRepository
    extends ExternalRelationRepository<ExternalRelationPartner> {

  @Query("Select m.relationNumber from ExternalRelationPartner m")
  List<Number> getAllRelationNumbers();

}
